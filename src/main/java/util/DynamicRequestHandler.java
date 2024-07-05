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
        Map<String, String> queryParams = request.getQueryParams();

        if (path.startsWith("/create")) {
            handleCreateRequest(queryParams, response);
        }

    }
    /**
     * Handles a create request.
     *
     * @param queryParams the query parameters
     * @param response the response to send
     */
    private void handleCreateRequest(Map<String, String> queryParams, HttpResponse response) {
        try {
            String userId = URLDecoder.decode(queryParams.get("userId"), "UTF-8");
            String password = URLDecoder.decode(queryParams.get("password"), "UTF-8");
            String name = URLDecoder.decode(queryParams.get("name"), "UTF-8");
            String email = URLDecoder.decode(queryParams.get("email"), "UTF-8");

            Database.addUser(new User(userId, password, name, email));

            logUserCreation();

            Map<String, String> headers = new HashMap<>();
            headers.put("Location", "/index.html");
            response.sendResponse(303, "See Other", headers, null);
        } catch (UnsupportedEncodingException e) {
            logger.debug("Error decoding query parameters: {}", e.getMessage());
            sendErrorResponse(response, 400, "Bad Request");
        }
    }

    /**
     * Logs the creation of a user.
     */
    private void logUserCreation() {
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