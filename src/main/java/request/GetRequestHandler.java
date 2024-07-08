package request;

import response.ResponseFactory;
import db.Database;
import http.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import static response.FileHandler.*;
import static response.ResponseFactory.response200Header;
import static response.ResponseFactory.response404Header;

public class GetRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(GetRequestHandler.class);
    static Properties properties = new Properties();
    private static String staticPath;

    public static void handler(String url, OutputStream out) throws IOException {

        try (InputStream input = GetRequestHandler.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                log.error("Unable to find config.properties");
            }
            properties.load(input);
            staticPath = properties.getProperty("staticPath");
        } catch (IOException e) {
            e.printStackTrace();
        }



        String[] splitUrl = url.split("\\?");
        String requestURL = splitUrl[0];

        switch (requestURL) {
            case "/", "/registration", "/login" -> sendResponse(staticPath + requestURL + "/index.html", out);

            default -> sendResponse(staticPath + requestURL, out);
        }
    }

    private static void createUser(String param, OutputStream out) {
        String[] params = param.split("&");

        String userId = params[0].split("=")[1];
        String password = params[1].split("=")[1];
        String name = params[2].split("=")[1];
        String email = params[3].split("=")[1];

        Database.addUser(new User(userId, password, name, email));

        DataOutputStream dos = new DataOutputStream(out);
        ResponseFactory.response302Header(dos, "/");
    }

    private static String getContentType(String type) {
        return switch (type) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "text/javascript";
            case "ico" -> "image/vnd.microsoft.icon";
            case "png" -> "image/png";
            case "jpg" -> "image/jpg";
            case "svg" -> "image/svg+xml";
            default -> "*/*"; // 이건 작동 안함 -> 406 에러 반환
        };
    }

    private static void sendResponse(String requestUrl, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseWithStatus response = getFileContent(requestUrl);

        HttpStatus status = response.status;
        byte[] body = response.body;

        switch (status) {
            case OK -> response200Header(dos, body.length, getContentType(type));
            case NOT_FOUND -> response404Header(dos, body.length);
        }
        ResponseFactory.responseBody(dos, body);
    }

}
