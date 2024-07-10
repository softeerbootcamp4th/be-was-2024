package webserver.mapper;

import model.UserCreate;
import model.UserLogin;
import webserver.RequestResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public class PostHandler {

    private static String redirectUrl = "/index.html";

    public static void handle(String url, byte[] body, RequestResponse requestResponse) throws IOException {
        switch (url) {
            case "/user/create":
                UserCreate.createUser(new String(body, "UTF-8"));
                requestResponse.redirectPath(redirectUrl);
            case "/login":
                boolean userExists = UserLogin.login(new String(body, "UTF-8"));
                if(!userExists){
                    requestResponse.redirectPath(redirectUrl);
                }
                redirectUrl = "/main/index.html";
                requestResponse.setCookieAndRedirectPath(redirectUrl,);
            default:
                break;
        }


    }


}
