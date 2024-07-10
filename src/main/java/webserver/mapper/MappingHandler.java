package webserver.mapper;

import webserver.HttpRequest;
import webserver.RequestResponse;
import java.io.IOException;

public class MappingHandler {

    public static void mapRequest(HttpRequest httpRequest, RequestResponse requestResponse) throws IOException {
        String method = httpRequest.getMethod();
        String url = httpRequest.getUrl();
        byte[] body = httpRequest.getBody();

        switch (method) {
            case "GET":
                GetHandler.handle(url, requestResponse);
                break;
            case "POST":
                PostHandler.handle(url, body, requestResponse);
                break;
            default:
                break;
        }
    }

}
