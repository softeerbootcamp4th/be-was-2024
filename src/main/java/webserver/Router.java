package webserver;

import db.Database;
import enums.FileType;
import enums.Status;
import model.User;
import utils.Handler;
import utils.HttpRequestParser;
import utils.HttpResponseHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

        Handler handler = getHandlersMap.get(httpRequestParser.getPath());
        if (handler != null) {
            return handler;
        }

        return this::invalidRequestHandler;
    }

    private void initGetHandlers() {
        getHandlersMap.put("/create", this::createUserHandler);
        getHandlersMap.put("/registration", this::mainHandler);
        getHandlersMap.put("/", this::mainHandler);
        getHandlersMap.put("/login", this::mainHandler);

    }

    private void initPostHandlers() {}

    private void createUserHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) {
        Map<String, String> queryParametersMap = httpRequestParser.getQueryParametersMap();
        String userId = queryParametersMap.get("userId");
        String password = queryParametersMap.get("password");
        String name = queryParametersMap.get("name");
        String email = queryParametersMap.get("email");

        User user = new User(userId, password, name, email);
        Database.addUser(user);

        httpResponseHandler
                .setStatus(Status.FOUND)
                .addHeader("Location", "http://localhost:8080/login")
                .respond(null);
    }

    private void mainHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) throws IOException {
        String path = httpRequestParser.getPath();
        String filePath = "/Users/jungwoo/Desktop/study/be-was-2024/src/main/resources/static" + path + "/index.html";
        byte[] body = readFileToByteArray(filePath);
        httpResponseHandler
                .setStatus(Status.OK)
                .addHeader("Content-Type", "text/html")
                .addHeader("Content-Length", String.valueOf(body.length))
                .respond(body);

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
                .respond(body);
    }

    private void invalidRequestHandler(HttpRequestParser httpRequestParser, HttpResponseHandler httpResponseHandler) {
        String responseBody = "<html><body><h1>404 Not Found</h1></body></html>";
        byte[] body = responseBody.getBytes(StandardCharsets.UTF_8);
        httpResponseHandler
                .setStatus(Status.NOT_FOUND)
                .addHeader("Content-Type", "text/html")
                .addHeader("Content-Length", String.valueOf(body.length))
                .respond(body);

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
}
