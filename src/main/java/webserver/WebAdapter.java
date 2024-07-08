package webserver;

import common.JsonBuilder;
import db.Database;
import exception.CannotResolveRequestException;
import model.User;
import web.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 요청 헤더에서 필요한 정보를 추출하는 클래스
 * TODO(키워드 프로퍼티 파일로 관리)
 */
public class WebAdapter {
    static final String ACCEPT = "Accept";
    static final String CONTENT_TYPE = "Content-Type";
    static final String CONTENT_LENGTH = "Content-Length";

    /**
     * request로 들어온 HTTP 요청을 한줄씩 파싱하여 적절한 HttpRequest 객체를 생성
     * @param request 요청 전문
     * @return HttpRequest 객체
     */
    public static HttpRequest parseRequest(String request) {
        HttpMethod method;
        String path, contentType = MIME.UNKNOWN.getType();
        LinkedList<String> accept = new LinkedList<>();
        int contentLength = 0;
        StringBuilder body = new StringBuilder();

        String[] requestLine = request.split("\n");

        // 첫줄에 HTTP Method 포함
        String[] line_1 = requestLine[0].split(" ");
        method = HttpMethod.valueOf(line_1[0]);
        path = line_1[1];

        for(int i=1; i<requestLine.length; i++) {
            String[] line_N = requestLine[i].split(":");
            // body
            if(line_N.length==1) {
                body.append(line_N[0]).append("\n");
            }
            // header
            else {
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
                }
            }
        }

        return new HttpRequest().method(method).path(path).accept(accept).contentLength(contentLength).contentType(contentType).body(body.toString()).build();
    }

    public static HttpResponse createResponse(ResponseCode code, String contentType) {
        return new HttpResponse.HttpResponseBuilder().code(code).contentType(contentType).build();
    }

    public static String resolveRequestUri(HttpRequest request, OutputStream out) throws IOException {
        if(request.isGetRequest()) {
            return resolveGetRequestUri(request.getPath(), out);
        } else if(request.isPostRequest()) {
            return resolvePostRequestUri(request.getPath(), request.getBody(), out);
        }

        throw new CannotResolveRequestException("Cannot Resolve Request Method");
    }

    /**
     * POST 요청을 처리
     */
    private static String resolvePostRequestUri(String restUri, String body, OutputStream out) throws IOException {
        // POST registration
        if(restUri.split("\\?")[0].equals("/signUp")) {
            Map<String, String> map = parseBodyInForm(body);
            Database.addUser(new User(map.get("userId"), map.get("password"), map.get("name"), ""));

            HttpResponse response = new HttpResponse.HttpResponseBuilder()
                    .code(ResponseCode.FOUND)
                    .build();

            out.write(response.getBytes());
        }

        return "/index.html";
    }

    private static Map<String, String> parseBodyInForm(String body) {
        HashMap<String, String> map = new HashMap<>();
        String[] chunks = body.split("&");
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
    private static String resolveGetRequestUri(String restUri, OutputStream out) throws IOException {
        // GET으로 회원가입 요청시 400 응답
        if(restUri.split("\\?")[0].equals("/signUp")) {

            HttpResponse response = new HttpResponse.HttpResponseBuilder()
                    .code(ResponseCode.BAD_REQUEST)
                    .build();

            out.write(response.getBytes());
        }
        // 유저 리스트 찾아서 json으로 반환
        if(restUri.split("\\?")[0].equals("/user/list")) {

            Collection<User> users = Database.findAll();
            String jsonUser = JsonBuilder.buildJsonResponse(users);

            HttpResponse response = new HttpResponse.HttpResponseBuilder()
                    .code(ResponseCode.OK)
                    .contentType(MIME.JSON.getType())
                    .contentLength(jsonUser.length())
                    .body(jsonUser)
                    .build();

            out.write(response.getBytes());
        }
        // 데이터베이스 초기화
        else if(restUri.split("\\?")[0].equals("/database/init")) {
            Database.initialize();

            HttpResponse response = new HttpResponse.HttpResponseBuilder()
                    .code(ResponseCode.OK)
                    .build();

            out.write(response.getBytes());
        }

        // 최종적으로 뷰를 찾아 반환
        return resolveGetRequestUri(restUri);
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
