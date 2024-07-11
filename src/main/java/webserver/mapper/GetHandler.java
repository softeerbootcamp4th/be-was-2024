package webserver.mapper;

import model.SessionIdControl;
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
            default:
                url = staticResourceDir + url;
                break;
        }
        requestResponse.openPath(url);
    }
}
