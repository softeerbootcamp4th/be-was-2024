package webserver;

import common.JsonBuilder;
import db.SessionDatabase;
import db.SessionIdMapper;
import db.UserDatabase;
import exception.CannotResolveRequestException;
import facade.UserFacade;
import model.Session;
import model.User;
import web.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 요청 헤더에서 필요한 정보를 추출하는 클래스
 * TODO(키워드 프로퍼티 파일로 관리)
 */
public class WebAdapter {
    static final String ACCEPT = "Accept";
    static final String CONTENT_TYPE = "Content-Type";
    static final String CONTENT_LENGTH = "Content-Length";
    static final String COOKIE = "cookie";
    static final String SESSION_ID = "SID";

    /**
     * request로 들어온 HTTP 요청을 한줄씩 파싱하여 적절한 HttpRequest 객체를 생성
     * @param request 요청 전문
     * @return HttpRequest
     */
    public static HttpRequest parseRequest(String request, byte[] body) {
        HttpMethod method;
        String path, contentType = MIME.UNKNOWN.getType();
        LinkedList<String> accept = new LinkedList<>();
        int contentLength = 0;
        Map<String, String> cookie = new HashMap<>();

        String[] requestLine = request.split("\n");

        // Request Line
        String[] line_1 = requestLine[0].split(" ");
        method = HttpMethod.valueOf(line_1[0]);
        path = line_1[1];

        for(int i=1; i<requestLine.length; i++) {
            if(requestLine[i].split(":").length==1) continue;
            String[] line_N = requestLine[i].split(":");
            String key = line_N[0].trim();
            String value = line_N[1].trim();

            // Accept헤더 MimeType 설정
            switch (key) {
                case ACCEPT -> {
                    String[] acceptLine = value.split(";");
                    String[] mimeType = acceptLine[0].split(",");
                    accept.addAll(Arrays.asList(mimeType));
                }
                case CONTENT_LENGTH -> contentLength = Integer.parseInt(value);
                case CONTENT_TYPE -> contentType = value;
                case COOKIE -> {
                    String[] cookies = value.split(";");
                    for(String c: cookies) {
                        String cookieName = c.split(":")[0].trim();
                        String cookieId = c.split(":")[1].trim();
                        cookie.put(cookieName, cookieId);
                    }
                }
            }
        }

        return new HttpRequest.HttpRequestBuilder()
                .method(method)
                .path(path)
                .accept(accept)
                .contentLength(contentLength)
                .contentType(contentType)
                .cookie(cookie)
                .body(body)
                .build();
    }

    public static HttpResponse createResponse(ResponseCode code, String contentType) {
        return new HttpResponse.HttpResponseBuilder().code(code).contentType(contentType).build();
    }

    public static String resolveRequestUri(HttpRequest request, OutputStream out) throws IOException {
        if(request.isGetRequest()) {
            return resolveGetRequestUri(request, out);
        } else if(request.isPostRequest()) {
            return resolvePostRequestUri(request, out);
        }

        throw new CannotResolveRequestException("Cannot Resolve Request Method");
    }

    /**
     * POST 요청을 처리
     */
    private static String resolvePostRequestUri(HttpRequest request, OutputStream out) throws IOException {
        // POST registration
        if(request.getPath().equals("/signUp")) {
            Map<String, String> map = parseBodyInForm(request.getBody()); // userId, password, name
            UserFacade.createUser(new User(map.get("userId"), map.get("password"), map.get("name"), ""));

            HttpResponse response = new HttpResponse.HttpResponseBuilder()
                    .code(ResponseCode.FOUND)
                    .build();

            response.writeInBytes(out);

        } else if(request.getPath().equals("/signIn")) {
            Map<String, String> map = parseBodyInForm(request.getBody()); // userId, password

            HttpResponse response;
            // 헤더에 세션이 존재하지 않을 경우 -> 새로 로그인하는 경우
            if(request.getCookie().isEmpty() || request.getCookie().get(SESSION_ID)==null) {
                if(UserFacade.isUserExist(map)) { // id, pw 일치하다면
                    // 세션 생성
                    Session session = SessionDatabase.createDefaultSession();
                    // 생성된 세션을 세션 저장소에 저장
                    SessionIdMapper.addSessionId(session.getId(), map.get("userId"));
                    Map<String, String> hashMap = new ConcurrentHashMap<>();
                    hashMap.put(SESSION_ID, session.getId());

                    response = new HttpResponse.HttpResponseBuilder()
                            .code(ResponseCode.FOUND)
                            .cookie(hashMap)
                            .build();

                } else { // id, pwd 불일치
                    response = new HttpResponse.HttpResponseBuilder()
                            .code(ResponseCode.FOUND)
                            .location("/login/index.html")
                            .build();
                }
            }
            // 세션이 존재하는 경우 -> 세션 유효성 확인
            else {
                SessionDatabase.removeExpiredSessions();
                String userId = SessionIdMapper.findUserIdBySessionId(request.getCookie().get(SESSION_ID));

                // 세션이 만료되었거나 세션 저장소에서 찾을 수 없음
                if(userId==null) {
                    response = new HttpResponse.HttpResponseBuilder()
                            .code(ResponseCode.FOUND)
                            .location("/login/index.html")
                            .build();
                } else {
                    response = new HttpResponse.HttpResponseBuilder()
                            .code(ResponseCode.FOUND)
                            .location("/login/index.html")
                            .build();
                }
            }


            response.writeInBytes(out);
        }

        return "/index.html";
    }

    private static Map<String, String> parseBodyInForm(byte[] body) {
        HashMap<String, String> map = new HashMap<>();
        String bodyStr = new String(body);
        String[] chunks = bodyStr.split("&");
        for (String chunk : chunks) {
            String key = chunk.split("=")[0];
            String value = chunk.split("=")[1];
            map.put(key, value);
        }
        return map;
    }

    /**
     * 비즈니스 로직 처리가 필요하다면 처리한 후, 뷰 응답
     */
    private static String resolveGetRequestUri(HttpRequest request, OutputStream out) throws IOException {
        // GET으로 회원가입 요청시 400 응답
        if(request.getPath().split("\\?")[0].equals("/signUp")) {

            HttpResponse response = new HttpResponse.HttpResponseBuilder()
                    .code(ResponseCode.BAD_REQUEST)
                    .build();

            response.writeInBytes(out);
        }
        // 유저 리스트 찾아서 json으로 반환
        if(request.getPath().split("\\?")[0].equals("/user/list")) {

            Collection<User> users = UserDatabase.findAll();
            String jsonUser = JsonBuilder.buildJsonResponse(users);

            HttpResponse response = new HttpResponse.HttpResponseBuilder()
                    .code(ResponseCode.OK)
                    .contentType(MIME.JSON.getType())
                    .contentLength(jsonUser.length())
                    .body(jsonUser.getBytes())
                    .build();

            response.writeInBytes(out);
        }
        // 데이터베이스 초기화
        else if(request.getPath().split("\\?")[0].equals("/database/init")) {
            UserDatabase.initialize();

            HttpResponse response = new HttpResponse.HttpResponseBuilder()
                    .code(ResponseCode.OK)
                    .build();

            response.writeInBytes(out);
        }

        // 최종적으로 뷰를 찾아 반환
        return resolveGetRequestUri(request.getPath());
    }

    /**
     * GET 요청에 적절한 뷰를 응답해준다
     */
    private static String resolveGetRequestUri(String restUri) {

        return switch(restUri) {
            case "/login" -> "/login/index.html";
            case "/registration" -> "/registration/index.html";
            case "/comment" -> "/comment/index.html";
            case "/article" -> "/article/index.html";
            default -> "/index.html";
        };
    }
}
