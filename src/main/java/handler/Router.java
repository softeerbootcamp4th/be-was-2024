package handler;

import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import util.TemplateEngine;
import util.exception.CustomException;

import java.io.IOException;

import static handler.GetHandler.*;
import static handler.PostHandler.createUser;
import static handler.PostHandler.loginUser;
import static util.Constants.*;

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
            case PATH_ROOT -> serveRootPage(httpRequest);
            case PATH_REGISTRATION, PATH_LOGIN, PATH_COMMENT -> serveStaticFile(requestTarget + FILE_INDEX);
            case PATH_ARTICLE -> GetHandler.postArticle(httpRequest);
            case PATH_LOGOUT -> logout(httpRequest);
            case PATH_USER + PATH_LIST -> getUserList(httpRequest);
            default -> serveStaticFile(requestTarget);
        };
    }

    private static HttpResponse postRequestMapping(HttpRequest httpRequest) throws IOException {
        String requestUrl = httpRequest.getRequestUrl();

        try {
            return switch (requestUrl) {
                case PATH_USER + PATH_CREATE -> createUser(httpRequest);
                case PATH_USER + PATH_LOGIN -> loginUser(httpRequest);
                case PATH_ARTICLE -> PostHandler.postArticle(httpRequest);
                default -> throw new IllegalStateException("Unexpected value: " + requestUrl);
            };
        } catch (CustomException e) {
            return TemplateEngine.showAlert(e.getMessage(), "/");
        }
    }
}
