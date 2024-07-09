package webserver.mapper;

import model.UserCreate;
import model.UserLogin;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostHandler {

    private static String redirectUrl = "/index.html";

    public static String handle(String url, byte[] body) throws IOException {
        switch (url) {
            case "/user/create":
                UserCreate.createUser(new String(body, "UTF-8"));
                return redirectUrl;
            case "/login":
                boolean userExists = UserLogin.login(new String(body, "UTF-8"));
                if(!userExists){
                    return redirectUrl;
                }
                return redirectUrl = "/main/index.html";
            default:
                break;
        }

        return null;
    }


}
