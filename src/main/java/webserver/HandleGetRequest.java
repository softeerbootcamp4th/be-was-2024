package webserver;

import db.Database;
import model.HttpStatus;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static webserver.FileHandler.*;
import static webserver.ResponseFactory.response200Header;
import static webserver.ResponseFactory.response404Header;

public class HandleGetRequest {
    private final static String staticPath = "./src/main/resources/static";
    private final static FileHandler fileHandler = new FileHandler();

    public static void handler(String url, OutputStream out) throws IOException {
        String[] splitUrl = url.split("\\?");
        String requestURL = splitUrl[0];

        switch (requestURL) {
            case "/", "/registration" -> sendResponse(staticPath + requestURL + "/index.html", out);
            case "/user/create" -> createUser(splitUrl[1], out);
            default -> sendResponse(staticPath + requestURL, out);
        }
    }

    private static void createUser(String param, OutputStream out) throws IOException {
        System.out.println("param = " + param);
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
            default -> "*/*";
        };
    }

    private static void sendResponse(String requestUrl, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseWithStatus response = getFileContent(requestUrl);

        HttpStatus status = response.status;
        byte[] body = response.body;

        switch (status){
            case OK -> response200Header(dos, body.length, getContentType(type));
            case NOT_FOUND -> response404Header(dos, body.length);
        }
        ResponseFactory.responseBody(dos, body);
    }

}
