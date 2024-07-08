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
 * 하드코딩이 많음
 * 모든 key값들을 enum으로 대체할 수는 있으나 일단 더 좋은 방법 생각중
 */
public class WebAdapter {

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
                if(key.equals("Accept")) {
                    String[] acceptLine = value.split(";");
                    String[] mimeType = acceptLine[0].split(",");
                    accept.addAll(Arrays.asList(mimeType));
                } else if(key.equals("Content-Length")) {
                    contentLength = Integer.parseInt(value);
                } else if(key.equals("Content-Type")) {
                    contentType = value;
                }
            }
        }

        return new HttpRequest().method(method).path(path).accept(accept).contentLength(contentLength).contentType(contentType).body(body.toString()).build();
    }

    public static HttpResponse createResponse(ResponseCode code, String contentType) {
        return new HttpResponse().code(code).contentType(contentType);
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

            String redirectResponse = "HTTP/1.1 302 Found\r\n" +
                    "Location: /\r\n" +
                    "Content-Length: 0\r\n" +
                    "\r\n";
            out.write(redirectResponse.getBytes());
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
            String redirectResponse = "HTTP/1.1 400 Bad Request\r\n" +
                    "Content-Length: 0\r\n" +
                    "\r\n";
            out.write(redirectResponse.getBytes());
        }
        // 유저 리스트 찾아서 json으로 반환
        if(restUri.split("\\?")[0].equals("/user/list")) {

            Collection<User> users = Database.findAll();
            String jsonUser = JsonBuilder.buildJsonResponse(users);

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + jsonUser.length() + "\r\n" +
                    "\r\n" +
                    jsonUser;
            out.write(response.getBytes());
        }
        // 데이터베이스 쵝화
        else if(restUri.split("\\?")[0].equals("/database/init")) {
            Database.initialize();

            String response = "HTTP/1.1 200 OK\r\n";
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
