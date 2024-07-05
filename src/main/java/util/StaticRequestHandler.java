package util;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequest;
import util.HttpResponse;
import util.HttpRequestParser;

import db.Database;
import model.User;

/**
 * Handles static requests.
 */
public class StaticRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticRequestHandler.class);

    /**
     * Handles the given request.
     *
     * @param request the request to handle
     * @param response the response to send
     */
    public void handle(HttpRequest request, HttpResponse response) {
        String url = request.getUrl();
        String contentType = request.getContentType();
        File file = new File(Config.ROOT_DIRECTORY + url);

        if (file.exists() && !file.isDirectory()) {
            sendFile(file, contentType, response);
        } else {
            send404Response(response);
        }
    }

    /**
     * Sends the given file as a response.
     *
     * @param file the file to send
     * @param contentType the content type of the file
     * @param response the response to send
     */
    private void sendFile(File file, String contentType, HttpResponse response) {
        byte[] body = readFileToByteArray(file);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", String.valueOf(body.length));
        response.sendResponse(200, "OK", headers, body);
    }

    /**
     * Sends a 404 Not Found response.
     */
    private void send404Response(HttpResponse response) {
        String body = "<html><body><h1>404 Not Found</h1></body></html>";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        headers.put("Content-Length", String.valueOf(body.length()));
        response.sendResponse(404, "Not Found", headers, body.getBytes());
    }

    /**
     * Reads the given file into a byte array.
     *
     * @param file the file to read
     * @return the byte array
     */
    private byte[] readFileToByteArray(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            return data;
        } catch (IOException e) {
            logger.error("Error reading file: {}", e.getMessage());
            return new byte[0];
        }
    }
}