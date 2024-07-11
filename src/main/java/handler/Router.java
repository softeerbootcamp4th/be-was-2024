package handler;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;

import java.io.IOException;

import static handler.GetHandler.*;
import static handler.PostHandler.createUser;
import static handler.PostHandler.loginUser;

public class Router {
    public static HttpResponse requestMapping(HttpRequest httpRequest) throws IOException {
        HttpMethod method = httpRequest.getHttpMethod();

        return switch (method) {
            case GET -> getRequestMapping(httpRequest);
            case POST -> postRequestMapping(httpRequest);
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };
    }

    private static HttpResponse getRequestMapping(HttpRequest httpRequest) throws IOException {
        String requestUrl = httpRequest.getRequestUrl();

        String[] splitUrl = requestUrl.split("\\?");
        String requestTarget = splitUrl[0];

        return switch (requestTarget) {
            case "/" -> serveRootPage(httpRequest);
            case "/registration", "/login", "/article", "/comment" -> serveStaticFile(requestTarget + "/index.html");
            case "/loginCheck" -> loginCheck(httpRequest);
            case "/logout" -> logout(httpRequest);
            case "/user/list" -> getUserList(httpRequest);
            default -> serveStaticFile(requestTarget);
        };
    }

    private static HttpResponse postRequestMapping(HttpRequest httpRequest) {
        String requestUrl = httpRequest.getRequestUrl();

        return switch (requestUrl) {
            case "/user/create" -> createUser(httpRequest);
            case "/user/login" -> loginUser(httpRequest);
            default -> throw new IllegalStateException("Unexpected value: " + requestUrl);
        };
    }
}
