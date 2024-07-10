package handler;

import http.HttpMethod;
import http.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;

import static handler.GetHandler.*;
import static handler.PostHandler.createUser;
import static handler.PostHandler.loginUser;

public class Router {
    public static void requestMapping(HttpRequest httpRequest, OutputStream out) throws IOException {
        HttpMethod method = httpRequest.getHttpMethod();

        switch (method){
            case GET -> getRequestMapping(httpRequest, out);
            case POST -> postRequestMapping(httpRequest, out);
        }
    }

    private static void getRequestMapping(HttpRequest httpRequest, OutputStream out) throws IOException {
        String requestUrl = httpRequest.getRequestUrl();

        String[] splitUrl = requestUrl.split("\\?");
        String requestTarget = splitUrl[0];

        switch (requestTarget) {
            case "/", "/registration", "/login" -> sendResponse(requestTarget + "/index.html", out);
            case "/loginCheck" -> loginCheck(httpRequest, out);
            case "/logout" -> logout(httpRequest, out);
            default -> sendResponse(requestTarget, out);
        }
    }

    private static void postRequestMapping(HttpRequest httpRequest, OutputStream out) throws IOException {
        String requestUrl = httpRequest.getRequestUrl();

        switch(requestUrl){
            case "/user/create" -> createUser(httpRequest, out);
            case "/user/login" -> loginUser(httpRequest, out);
        }
    }
}
