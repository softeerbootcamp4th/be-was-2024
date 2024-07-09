package webserver.mapper;

import webserver.HttpRequest;
import webserver.RequestResponse;

import java.io.DataOutputStream;
import java.io.IOException;

public class MappingHandler {

    private static final String staticResourceDir = System.getProperty("staticResourceDir");

    public static void mapRequest(HttpRequest httpRequest, RequestResponse requestResponse) throws IOException {
        String method = httpRequest.getMethod();
        String url = httpRequest.getUrl();
        byte[] body = httpRequest.getBody();

        switch (method) {
            case "GET":
                url = GetHandler.handle(url);
                requestResponse.openPath(url);
                break;
            case "POST":
                url = PostHandler.handle(url, body);
                requestResponse.redirectPath(url);
                break;
            default:
                break;
        }
    }

}
