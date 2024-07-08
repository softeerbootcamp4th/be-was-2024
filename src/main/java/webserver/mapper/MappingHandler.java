package webserver.mapper;

import webserver.HttpRequest;

import java.io.DataOutputStream;
import java.io.IOException;

public class MappingHandler {

    private static final String staticResourceDir = System.getProperty("staticResourceDir");

    public static String mapRequest(HttpRequest httpRequest, DataOutputStream dos) throws IOException {
        String method = httpRequest.getMethod();
        String url = httpRequest.getUrl();
        byte[] body = httpRequest.getBody();

        switch (method) {
            case "GET":
                url = GetHandler.handle(url);
                break;
            case "POST":
                PostHandler.handle(url, body, dos);
                break;
            default:
                break;
        }

        return staticResourceDir + url;
    }

}
