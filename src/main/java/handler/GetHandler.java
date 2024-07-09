package handler;

import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.GetRequestHandler;
import util.Utils.ResponseWithStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import static util.ResponseFactory.*;
import static util.ResponseFactory.addHeader;
import static util.Utils.*;

public class GetHandler {
    private static final Logger log = LoggerFactory.getLogger(GetHandler.class);
    private static final Properties properties = new Properties();
    private static String staticPath = getStaticPath();

    static {
        try (InputStream input = GetRequestHandler.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                log.error("Unable to find config.properties");
            }
            properties.load(input);
            staticPath = properties.getProperty("staticPath");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getRouter(String requestUrl, OutputStream out) throws IOException {
        String[] splitUrl = requestUrl.split("\\?");
        String requestURL = splitUrl[0];

        switch (requestURL) {
            case "/", "/registration", "/login" -> sendResponse(staticPath + requestURL + "index.html", out);
            default -> sendResponse(staticPath + requestURL, out);
        }
    }

    private static void sendResponse(String requestUrl, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseWithStatus responseWithStatus = getFileContent(requestUrl);

        HttpStatus status = responseWithStatus.status;
        byte[] body = responseWithStatus.body;

        addHeader(dos, status);
        addContentType(dos, getContentType(type));
        addContentLength(dos, body.length);
        responseBody(dos, body);
    }
}
