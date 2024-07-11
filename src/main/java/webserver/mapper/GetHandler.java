package webserver.mapper;

import db.Session;
import model.SessionIdControl;
import model.User;
import model.UserInfoExtract;
import webserver.HttpRequest;
import webserver.RequestResponse;

import java.io.IOException;
import java.util.Map;

public class GetHandler {

    private static final String staticResourceDir = System.getProperty("staticResourceDir");

    public static synchronized void handle(HttpRequest httpRequest, RequestResponse requestResponse) throws IOException {
        String url = httpRequest.getUrl();
        Map<String, String> headers = httpRequest.getHeaders();
        switch (url) {
            case "/registration":
                url = staticResourceDir + "/registration/index.html";
                break;
            case "/login":
                url = staticResourceDir + "/login/index.html";
                break;
            case "/main/index.html":
                String sessionId = UserInfoExtract.extractSessionIdFromHeader(headers.get("Cookie"));
                User user = Session.findUserBySessionId(sessionId);
                url = staticResourceDir + "/main/index.html";
                requestResponse.openPathWithUsername(url, user.getName());
                return;
            default:
                url = staticResourceDir + url;
                break;
        }
        requestResponse.openPath(url);
    }
}
