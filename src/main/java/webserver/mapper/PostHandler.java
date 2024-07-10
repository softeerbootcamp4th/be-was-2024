package webserver.mapper;

import model.SessionIdCreate;
import model.User;
import model.UserCreate;
import model.UserLogin;
import webserver.RequestResponse;

import java.io.IOException;

public class PostHandler {

    private static String redirectUrl = "/index.html";

    public static void handle(String url, byte[] body, RequestResponse requestResponse) throws IOException {
        switch (url) {
            case "/user/create":
                UserCreate.createUser(new String(body, "UTF-8"));
                requestResponse.redirectPath(redirectUrl);
                break;
            case "/login":
                User user = UserLogin.login(new String(body, "UTF-8"));
                if(user == null){
                    requestResponse.redirectPath(redirectUrl);
                    break;
                }
                redirectUrl = "/main/index.html";
                String sessionId = SessionIdCreate.nextSessionId(user);
                requestResponse.setCookieAndRedirectPath(sessionId, redirectUrl);
                break;
            default:
                break;
        }


    }


}
