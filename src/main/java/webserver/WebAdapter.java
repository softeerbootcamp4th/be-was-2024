package webserver;

import common.JsonBuilder;
import common.StringUtils;
import db.SessionDatabase;
import db.SessionIdMapper;
import db.UserDatabase;
import exception.CannotResolveRequestException;
import facade.SessionFacade;
import facade.UserFacade;
import common.ResponseUtils;
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
    static final String COOKIE = "Cookie";
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

        // Header
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
                        String cookieName = c.split("=")[0].trim();
                        String cookieId = c.split("=")[1].trim();
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
        if(request.getPath().equals(RestUri.SIGN_UP.getUri())) {
            // body의 유저 정보 파싱
            Map<String, String> map = StringUtils.parseBodyInForm(request.getBody());
            // 유저 생성
            UserFacade.createUser(new User(map.get("userId"), map.get("password"), map.get("name"), ""));

            HttpResponse response = ResponseUtils.redirectToView(ViewPath.DEFAULT);
            response.writeInBytes(out);

        } else if(request.getPath().equals(RestUri.SIGN_IN.getUri())) {
            Map<String, String> map = StringUtils.parseBodyInForm(request.getBody()); // userId, password
            HttpResponse response;

            if(UserFacade.isUserExist(map)) { // id, pw 일치하다면
                // 세션 생성
                Session session = SessionDatabase.createDefaultSession();
                // 생성된 세션을 세션 저장소에 저장
                SessionIdMapper.addSessionId(session.getId(), map.get("userId"));
                Map<String, String> hashMap = new ConcurrentHashMap<>();
                hashMap.put(SESSION_ID, session.getId());

                response = ResponseUtils.redirectToViewWithCookie(hashMap);

            } else { // id, pwd 불일치
                response = ResponseUtils.redirectToView(ViewPath.LOGIN);
            }

            response.writeInBytes(out);

        } else if(request.getPath().equals(RestUri.LOGOUT.getUri())) {
            // 세션 저장소의 정보 삭제
            SessionFacade.invalidateAndRemoveSession(request);

            HttpResponse response = ResponseUtils.redirectToView(ViewPath.LOGIN);
            response.writeInBytes(out);
        }

        return ViewPath.DEFAULT.getFilePath();
    }



    /**
     * 비즈니스 로직 처리가 필요하다면 처리한 후, 뷰 응답
     */
    private static String resolveGetRequestUri(HttpRequest request, OutputStream out) throws IOException {
        // GET으로 회원가입 요청시 400 응답
        if(request.getPathWithoutQueryParam().equals(RestUri.SIGN_UP.getUri())) {
            HttpResponse response = ResponseUtils.responseBadRequest();
            response.writeInBytes(out);
        }
        // 유저 리스트 찾아서 json으로 반환
        if(request.getPathWithoutQueryParam().equals(RestUri.USER_LIST.getUri())) {
            Collection<User> users = UserDatabase.findAll();
            String jsonUser = JsonBuilder.buildJsonResponse(users);

            HttpResponse response = ResponseUtils.responseSuccessWithJson(jsonUser.length(), jsonUser.getBytes());
            response.writeInBytes(out);
        }
        // 데이터베이스 초기화
        else if(request.getPathWithoutQueryParam().equals(RestUri.DATABASE_INIT.getUri())) {
            UserDatabase.initialize();

            HttpResponse response = ResponseUtils.responseSuccess();
            response.writeInBytes(out);
        }

        // 최종적으로 뷰를 찾아 반환
        return resolveGetRequestUri(request.getPath());
    }

    /**
     * GET 요청에 적절한 뷰를 응답해준다
     */
    private static String resolveGetRequestUri(String restUri) {
        return ViewPath.findByRequestUri(restUri).getFilePath();
    }
}
