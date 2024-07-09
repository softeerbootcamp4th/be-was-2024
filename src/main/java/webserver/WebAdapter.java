package webserver;

import common.JsonBuilder;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpMethod;
import web.HttpRequest;
import web.HttpResponse;
import web.ResponseCode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class WebAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * request로 들어온 HTTP 요청을 한줄씩 파싱하여 적절한 HttpRequest 객체를 생성
     * @param request 요청 전문
     * @return HttpRequest 객체
     */
    public static HttpRequest parseRequest(String request) {
        HttpMethod method;
        String path;
        LinkedList<String> accept = new LinkedList<>();

        String[] requestLine = request.split("\n");

        // 첫줄에 HTTP Method 포함
        String[] line_1 = requestLine[0].split(" ");
        method = HttpMethod.valueOf(line_1[0]);
        path = line_1[1];

        for(int i=1; i<requestLine.length; i++) {
            String[] line_N = requestLine[i].split(":");
            String key = line_N[0].trim();
            String value = line_N[1].trim();

            // Accept헤더 MimeType 설정
            if(key.equals("Accept")) {
                String[] acceptLine = value.split(";");
                String[] mimeType = acceptLine[0].split(",");
                accept.addAll(Arrays.asList(mimeType));
            }
        }

        return new HttpRequest().method(method).path(path).accept(accept).build();
    }

    public static HttpResponse createResponse(ResponseCode code, String contentType) {
        return new HttpResponse().code(code).contentType(contentType);
    }

    /**
     * handle specific requests
     */
    public static String resolveRequestUri(String restUri, OutputStream out) throws IOException {
        // registration
        if(restUri.split("\\?")[0].equals("/user/create")) {
            String userId = restUri.split("\\?")[1].split("&")[0].split("=")[1];
            String nickname = restUri.split("\\?")[1].split("&")[1].split("=")[1];
            String password = restUri.split("\\?")[1].split("&")[2].split("=")[1];
            Database.addUser(new User(userId, password, nickname, null));
            logger.info("User Added - Database.findAll().size() = {}", Database.findAll().size());

            String redirectResponse = "HTTP/1.1 302 Found\r\n" +
                    "Location: /\r\n" +
                    "Content-Length: 0\r\n" +
                    "\r\n";
            out.write(redirectResponse.getBytes());
            return "/index.html";
        }
        // get user list
        else if(restUri.split("\\?")[0].equals("/user/list")) {

            Collection<User> users = Database.findAll();
            String jsonUser = JsonBuilder.buildJsonResponse(users);

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + jsonUser.length() + "\r\n" +
                    "\r\n" +
                    jsonUser;
            out.write(response.getBytes());
            return "/index.html";
        }
        // initialize database
        else if(restUri.split("\\?")[0].equals("/database/init")) {
            Database.initialize();

            String response = "HTTP/1.1 200 OK\r\n";
            out.write(response.getBytes());
            return "/index.html";
        }


        return resolveRequestUri(restUri);
    }

    /**
     * map request uri to proper view
     */
    private static String resolveRequestUri(String restUri) {

        return switch(restUri) {
            case "/login" -> "/login/index.html";
            case "/registration" -> "/registration/index.html";
            case "/comment" -> "/comment/index.html";
            case "/article" -> "/article/index.html";
            default -> "/index.html";
        };
    }
}
