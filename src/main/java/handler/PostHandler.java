package handler;

import db.ArticleDatabase;
import db.Session;
import http.HttpResponse;
import processer.UserProcessor;
import util.exception.CustomException;
import http.HttpRequest;
import http.HttpStatus;

import java.util.HashMap;

import static util.Constants.*;
import static util.TemplateEngine.showAlert;
import static util.Utils.cookieParsing;

public class PostHandler {
    static HttpResponse createUser(HttpRequest httpRequest) {
        String body = new String(httpRequest.getBody());

        String[] bodyTokens = body.split(REG_AMP);

        HttpStatus httpStatus;
        byte[] responseBody = new byte[0];
        try {
            String userId = bodyTokens[0].split(REG_EQ, 2)[1];
            String name = bodyTokens[1].split(REG_EQ)[1];
            String password = bodyTokens[2].split(REG_EQ)[1];
            String email = bodyTokens[3].split(REG_EQ)[1];

            UserProcessor.createUser(userId, name, password, email);

            httpStatus = HttpStatus.FOUND;
        } catch (CustomException e) {
            httpStatus = e.getHttpStatus();
            responseBody = showAlert(e.getMessage(), PATH_HOST + PATH_REGISTRATION);
        } catch (ArrayIndexOutOfBoundsException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            responseBody = showAlert("모든 필드를 입력하세요.", PATH_HOST + PATH_REGISTRATION);
        }

        return new HttpResponse()
                .addStatus(httpStatus)
                .addHeader(LOCATION, PATH_ROOT)
                .addHeader(CONTENT_LENGTH, String.valueOf(responseBody.length))
                .addHeader(CONTENT_TYPE, TEXT_HTML)
                .addBody(responseBody);
    }

    static HttpResponse loginUser(HttpRequest httpRequest) {
        String body = new String(httpRequest.getBody());

        String[] bodyTokens = body.split(REG_AMP);


        HttpResponse response = new HttpResponse();
        byte[] responseBody = new byte[0];

        try {
            String userId = bodyTokens[0].split(REG_EQ)[1];
            String password = bodyTokens[1].split(REG_EQ)[1];

            UserProcessor.loginUser(userId, password);
            String sid = Session.createSession(userId);

            response.addStatus(HttpStatus.FOUND)
                    .addHeader(LOCATION, PATH_ROOT)
                    .addHeader(SET_COOKIE, "sid=" + sid + "; Path=/");
        } catch (CustomException e) {
            responseBody = showAlert(e.getMessage(), PATH_HOST + PATH_LOGIN);
            response.addStatus(e.getHttpStatus())
                    .addBody(responseBody);
        } catch (ArrayIndexOutOfBoundsException e) {
            responseBody = showAlert("모든 필드를 입력하세요.", PATH_HOST + PATH_LOGIN);
            response.addStatus(HttpStatus.BAD_REQUEST)
                    .addBody(responseBody);
        }

        response.addHeader(CONTENT_LENGTH, String.valueOf(responseBody.length))
                .addHeader(CONTENT_TYPE, TEXT_HTML);
        return response;
    }

    static HttpResponse postArticle(HttpRequest httpRequest) {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);

        String userId = Session.getUser(sid);

        String body = new String(httpRequest.getBody());
        String text = body.split("=")[1].trim();

        ArticleDatabase.createArticle(userId, text, "");

        return new HttpResponse()
                .addStatus(HttpStatus.FOUND)
                .addHeader(LOCATION, PATH_ROOT)
                .addBody(new byte[0]);
    }

}
