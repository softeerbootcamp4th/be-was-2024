package webserver.mapper;

import model.UserCreate;
import model.UserLogin;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostHandler {

    public static void handle(String url, byte[] body, DataOutputStream dos) throws IOException {
        switch (url) {
            case "/user/create":
                UserCreate.createUser(new String(body, "UTF-8"));
                RedirectHandler.redirectPath("/index.html", dos);
                break;
            case "/login":
                boolean userExists = UserLogin.login(new String(body, "UTF-8"));
                if(!userExists){
                    RedirectHandler.redirectPath("/index.html", dos);
                    break;
                }
                RedirectHandler.redirectPath("/main/index.html", dos);
            default:
                break;
        }
    }


}
