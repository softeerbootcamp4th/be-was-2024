package webserver.mapper;

import db.BoardDatabase;
import db.Session;
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
        User user = null;
        try {
            switch (url) {
                case "/user/create":
                    UserCreate.createUser(new String(body, "UTF-8"));
                    requestResponse.redirectPath(redirectUrl);
                    break;
                case "/login":
                    user = UserLogin.login(new String(body, "UTF-8"));
                    if(user == null){
                        requestResponse.redirectPath(redirectUrl);
                        break;
                    }
                    redirectUrl = "/main/index.html";
                    sessionId = SessionIdControl.createSessionId(user);
                    requestResponse.setCookieAndRedirectPath(sessionId, redirectUrl);
                    break;
                case "/logout":
                    sessionId = UserInfoExtract.extractSessionIdFromHeader(headers.get("cookie"));
                    SessionIdControl.deleteSessionId(sessionId);
                    redirectUrl = "/index.html";
                    requestResponse.resetCookieAndRedirectPath(redirectUrl);
                    break;
                case "/board/create":
                    sessionId = UserInfoExtract.extractSessionIdFromHeader(headers.get("cookie"));
                    user = Session.findUserBySessionId(sessionId);
                    String content = new String(body, "UTF-8").split("=")[1];
                    BoardDatabase.addBoard(new Board(user.getUserId(), content));
                    redirectUrl = "/main/index.html";
                    requestResponse.redirectPath(redirectUrl);
                    break;
                default:
                    break;
            }
        }catch (Exception e) {
            requestResponse.sendErrorPage("Invalid information has been entered", redirectUrl);
        }

    }

}
