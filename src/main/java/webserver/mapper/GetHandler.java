package webserver.mapper;

import webserver.RequestResponse;

import java.io.IOException;

public class GetHandler {

    private static final String staticResourceDir = System.getProperty("staticResourceDir");

    public static void handle(String url, RequestResponse requestResponse) throws IOException {
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
