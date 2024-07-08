package util;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequest;
import util.HttpResponse;
import util.HttpRequestParser;

import db.Database;
import model.User;
/**
 * Handles dynamic requests.
 */
public class DynamicRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(DynamicRequestHandler.class);

    /**
     * Handles the given request.
     *
     * @param request the request to handle
     * @param response the response to send
     */
    public void handle(HttpRequest request, HttpResponse response) {
        String path = request.getPath();
        String method = request.getMethod();
        Map<String, String> queryParams = request.getQueryParams();

        if (path.equals("/create") && method.equals("POST")) {
            handleCreateUserRequest(request, response);
        }

    }
    /**
     * Handles a create request.
     *
     * @param queryParams the query parameters
     * @param response the response to send
     */
    private void handleCreateUserRequest(HttpRequest request, HttpResponse response) {
        try {
            // POST 데이터 파싱
            String body = request.getBody();
            Map<String, String> params = parseFormData(body);

            String userId = params.get("userId");
            String password = params.get("password");
            String name = params.get("name");
            String email = params.get("email");

            // 사용자 생성
            Database.addUser(new User(userId, password, name, email));

            logUserCreation();

            // 리다이렉트 응답
            Map<String, String> headers = new HashMap<>();
            headers.put("Location", "/index.html");
            response.sendResponse(302, "Found", headers, null);
        } catch (Exception e) {
            logger.error("Error handling create user request: {}", e.getMessage());
            sendErrorResponse(response, 400, "Bad Request");
        }
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<>();
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
            String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
            params.put(key, value);
        }
        return params;
    }


    /**
     * Logs the creation of a user.
     */
    private void logUserCreation() {
        logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!User created");
        for (User user : Database.findAll()) {
            logger.debug("User created: {}", user);
        }
    }

    /**
     * Sends an error response.
     *
     * @param response the response to send
     * @param statusCode the status code
     * @param statusMessage the status message
     */
    private void sendErrorResponse(HttpResponse response, int statusCode, String statusMessage) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/plain");
        String body = statusMessage;
        response.sendResponse(statusCode, statusMessage, headers, body.getBytes());
    }
}