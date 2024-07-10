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
