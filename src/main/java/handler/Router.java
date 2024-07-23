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

/**
 * ttp 요청을 라우팅하는 클래스입니다.
 */
public class Router {
    /**
     * HttpRequset의 HttpMethod 필드를 통해 라우팅합니다.
     * @param httpRequest Http 요청을 담은 HttpRequest 객체입니다.
     * @return Get, Post 요청 시 해당 요청의 라우터를 호출하고, 그 외의 메소드 호출 시 IllegalStateException을 반환합니다.
     * @throws IOException
     */
    public static HttpResponse requestMapping(HttpRequest httpRequest) throws IOException {
        HttpMethod method = httpRequest.getHttpMethod();

        return switch (method) {
            case GET -> getRequestMapping(httpRequest);
            case POST -> postRequestMapping(httpRequest);
            default -> throw new IllegalStateException("Unexpected value: " + method);
        };
    }

    /**
     * Get 요청을 라우팅하는 메서드입니다.
     * @param httpRequest Http 요청을 담은 HttpRequest 객체입니다.
     * @return 요청 경로에 따라 해당하는 GetHandler의 메서드를 호출합니다.
     * @throws IOException
     */
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
    /**
     * Post 요청을 라우팅하는 메서드입니다.
     * @param httpRequest Http 요청을 담은 HttpRequest 객체입니다.
     * @return 요청 경로에 따라 해당하는 PostHandler의 메서드를 호출합니다.
     * @throws IOException
     */
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
