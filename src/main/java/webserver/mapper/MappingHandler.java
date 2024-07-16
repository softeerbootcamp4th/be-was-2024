package webserver.mapper;

import webserver.HttpRequest;
import webserver.RequestResponse;
import webserver.enumPackage.HttpStatus;

import java.io.IOException;

public class MappingHandler {

    public static void mapRequest(HttpRequest httpRequest, RequestResponse requestResponse) throws IOException {
        String method = httpRequest.getMethod();

        switch (method) {
            case "GET":
                GetHandler.handle(httpRequest, requestResponse);
                break;
            case "POST":
                PostHandler.handle(httpRequest, requestResponse);
                break;
            default:
                requestResponse.sendErrorPage(HttpStatus.METHOD_NOT_ALLOWED.getMessage(), "/");
                break;
        }
    }

}
