package handler;

import http.HttpMethod;
import http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

import static handler.GetHandler.getRouter;
import static handler.GetHandler.sendResponse;
import static handler.PostHandler.postRouter;

public class Router {
    public static void requestMapping(HttpRequest httpRequest, OutputStream out) throws IOException {
        HttpMethod method = httpRequest.getHttpMethod();

        switch (method){
            case GET -> getRequestMapping(httpRequest, out);
            case POST -> postRouter(httpRequest, out);
        }
    }

    private static void getRequestMapping(HttpRequest httpRequest, OutputStream out) throws IOException {
        String requestUrl = httpRequest.getRequestUrl();

        String[] splitUrl = requestUrl.split("\\?");
        String requestTarget = splitUrl[0];

        switch (requestTarget) {
            case "/", "/registration", "/login" -> sendResponse(requestTarget + "/index.html", out);
            default -> sendResponse(requestTarget, out);
        }
    }
}
