package webserver.mapper;

import model.User;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostHandler {

    public static void handle(String url, byte[] body, DataOutputStream dos) throws IOException {
        switch (url) {
            case "/user/create":
                User.createUser(new String(body, "UTF-8"));
                RedirectHandler.redirectPath("/index.html", dos);
                break;
            default:
                break;
        }
    }


}
