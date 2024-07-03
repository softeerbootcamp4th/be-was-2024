package webserver;

import common.JsonBuilder;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public class WebAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * treat various request cases - especially registration form
     */
    public String resolveRequestUri(String restUri, OutputStream out) throws IOException {
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
        } else if(restUri.split("\\?")[0].equals("/user/list")) {

            Collection<User> users = Database.findAll();
            String jsonUser = JsonBuilder.buildJsonResponse(users);

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: application/json\r\n" +
                    "Content-Length: " + jsonUser.length() + "\r\n" +
                    "\r\n" +
                    jsonUser;
            out.write(response.getBytes());
            return "/index.html";
        } else if(restUri.split("\\?")[0].equals("/database/init")) {
            Database.initialize();

            String response = "HTTP/1.1 200 OK\r\n";
            out.write(response.getBytes());
            return "/index.html";
        } else {
            return resolveRequestUri(restUri);
        }
    }

    /**
     * map request uri to proper view uri
     */
    private String resolveRequestUri(String restUri) {

        return switch(restUri) {
            case "/login" -> "/login/index.html";
            case "/registration" -> "/registration/index.html";
            case "/comment" -> "/comment/index.html";
            case "/article" -> "/article/index.html";
            default -> "/index.html";
        };
    }
}
