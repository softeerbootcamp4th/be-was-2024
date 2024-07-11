package webserver;

import db.Database;
import enums.FileType;
import enums.Status;
import model.User;
import utils.Cookie;
import utils.Handler;
import utils.HttpRequestParser;
import utils.HttpResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Handler> getHandlersMap = new HashMap<>();
    private final Map<String, Handler> postHandlersMap = new HashMap<>();
    public Router() {
        initGetHandlers();
        initPostHandlers();
    }

    public Handler getHandler(HttpRequestParser httpRequestParser) throws IOException {
        String extension = httpRequestParser.getExtension();
        if (extension != null) {
            return this::staticResourceHandler;
        }

        Handler handler = null;

        switch (httpRequestParser.getMethod()) {
            case GET -> handler = getHandlersMap.get(httpRequestParser.getPath());
            case POST -> handler = postHandlersMap.get(httpRequestParser.getPath());
        }

        if (handler != null) {
            return handler;
        }

        return this::invalidRequestHandler;
    }

    private void initGetHandlers() {
        getHandlersMap.put("/registration", this::mainHandler);
        getHandlersMap.put("/", this::mainHandler);
        getHandlersMap.put("/login", this::mainHandler);
        getHandlersMap.put("/main", this::mainHandler);
    }

    private void initPostHandlers() {
        postHandlersMap.put("/user/create", this::createUserHandler);
        postHandlersMap.put("/login", this::loginHandler);
        postHandlersMap.put("/logout", this::logoutHandler);
    }

    private void createUserHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) {
        Map<String, String> formData = getFormData(httpRequestParser);

        String userId = formData.get("userId");
        String password = formData.get("password");
        String name = formData.get("name");
        String email = formData.get("email");

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        httpResponseHandler
                .setStatus(Status.FOUND)
                .addHeader("Location", "http://localhost:8080")
                .respond();
    }

    private void mainHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) throws IOException {
        String path = httpRequestParser.getPath();
        String filePath = "/Users/jungwoo/Desktop/study/be-was-2024/src/main/resources/static" + path + "/index.html";
        byte[] body = readFileToByteArray(filePath);
        httpResponseHandler
                .setStatus(Status.OK)
                .addHeader("Content-Type", "text/html")
                .addHeader("Content-Length", String.valueOf(body.length))
                .setBody(body)
                .respond();

    }

    private void loginHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) throws IOException {
        Map<String, String> formData = getFormData(httpRequestParser);

        String userId = formData.get("userId");
        String password = formData.get("password");

        User user = Database.findUserById(userId);

        // 로그인 성공
        if (user != null && user.getPassword().equals(password)) {
            String sessionId = SessionManager.createSession(user);
            Cookie cookie = new Cookie.Builder("sid", sessionId)
                    .path("/")
                    .build();

            httpResponseHandler
                    .setStatus(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/main")
                    .addCookie(cookie)
                    .respond();
        } else {
            // 로그인 실패
            String filePath = "/Users/jungwoo/Desktop/study/be-was-2024/src/main/resources/static/login/login_failed.html";
            byte[] body = readFileToByteArray(filePath);
            httpResponseHandler
                    .setStatus(Status.UNAUTHORIZED)
                    .addHeader("Content-Type", FileType.getContentTypeByExtension("html"))
                    .addHeader("Content-Length", String.valueOf(body.length))
                    .setBody(body)
                    .respond();
        }
    }

    private void logoutHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) {
        Map<String, String> cookiesMap = httpRequestParser.getCookiesMap();
        String sessionId = cookiesMap.get("sid");
        if (sessionId != null) {
            Cookie cookie = new Cookie.Builder("sid", sessionId)
                    .path("/")
                    .maxAge(0)
                    .build();

            httpResponseHandler
                    .setStatus(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/")
                    .addCookie(cookie)
                    .respond();
        }
    }

    private void staticResourceHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) throws IOException {
        // 정적 리소스 반환
        String path = httpRequestParser.getPath();
        String extension = httpRequestParser.getExtension();
        String filePath = "/Users/jungwoo/Desktop/study/be-was-2024/src/main/resources/static" + path;
        byte[] body = readFileToByteArray(filePath);
        httpResponseHandler
                .setStatus(Status.OK)
                .addHeader("Content-Type", FileType.getContentTypeByExtension(extension))
                .addHeader("Content-Length", String.valueOf(body.length))
                .setBody(body)
                .respond();
    }

    private void invalidRequestHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) {
        String responseBody = "<html><body><h1>404 Not Found</h1></body></html>";
        byte[] body = responseBody.getBytes();
        httpResponseHandler
                .setStatus(Status.NOT_FOUND)
                .addHeader("Content-Type", "text/html")
                .addHeader("Content-Length", String.valueOf(body.length))
                .setBody(body)
                .respond();

    }

    private byte[] readFileToByteArray(String filePath) throws IOException{
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    private Map<String, String> getFormData(HttpRequestParser httpRequestParser) {
        String body = new String(httpRequestParser.getBody());
        Map<String, String> formData = new HashMap<>();
        for (String keyValue : body.split("&")) {
            int equalsIndex = keyValue.indexOf("=");
            if (equalsIndex != -1) {
                formData.put(keyValue.substring(0, equalsIndex), keyValue.substring(equalsIndex + 1));
            }
        }

        return formData;
    }
}
