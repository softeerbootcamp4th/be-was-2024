package webserver.mapper;

import model.*;
import webserver.HttpRequest;
import webserver.RequestResponse;
import java.io.IOException;
import java.util.Map;

public class PostHandler {

    private static String redirectUrl = "/index.html";
    private static String sessionId;

    public static void handle(HttpRequest httpRequest, RequestResponse requestResponse) throws IOException {
        String url = httpRequest.getUrl();
        byte[] body = httpRequest.getBody();
        Map<String, String> headers = httpRequest.getHeaders();
        try {
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
                    sessionId = SessionIdControl.createSessionId(user);
                    requestResponse.setCookieAndRedirectPath(sessionId, redirectUrl);
                    break;
                case "/logout":
                    sessionId = UserInfoExtract.extractSessionIdFromHeader(headers.get("Cookie"));
                    SessionIdControl.deleteSessionId(sessionId);
                    redirectUrl = "/index.html";
                    requestResponse.resetCookieAndRedirectPath(redirectUrl);
                    break;
                default:
                    break;
            }
        }catch (Exception e) {
            requestResponse.sendErrorPage("Invalid information has been entered", redirectUrl);
        }

    }

}
