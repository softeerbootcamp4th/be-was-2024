package webserver;

import db.Database;
import model.User;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HandleGetRequest {
    private final static String staticPath = "./src/main/resources/static";

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
        ResponseFactory.response302(dos, "/");
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
        byte[] body = FileHandler.getFileContent(requestUrl);

        String[] tokens = requestUrl.split("\\.");
        String type = tokens[tokens.length - 1];

        ResponseFactory.response200Header(dos, body.length, getContentType(type));
        ResponseFactory.responseBody(dos, body);
    }

}
