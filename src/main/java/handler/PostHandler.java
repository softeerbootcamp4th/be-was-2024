package handler;

import db.Session;
import http.HttpResponse;
import processer.UserProcessor;
import util.exception.CustomException;
import http.HttpRequest;
import http.HttpStatus;

import static util.TemplateEngine.showAlert;

public class PostHandler {
    static HttpResponse createUser(HttpRequest httpRequest) {
        String body = new String(httpRequest.getBody());

        String[] bodyTokens = body.split("&");

        HttpStatus httpStatus;
        byte[] responseBody = new byte[0];
        try {
            String userId = bodyTokens[0].split("=", 2)[1];
            String name = bodyTokens[1].split("=")[1];
            String password = bodyTokens[2].split("=")[1];
            String email = bodyTokens[3].split("=")[1];

            UserProcessor.createUser(userId, name, password, email);

            httpStatus = HttpStatus.FOUND;
        } catch (CustomException e) {
            httpStatus = e.getHttpStatus();
            responseBody = showAlert(e.getMessage(), "http://localhost:8080/registration");
        } catch (ArrayIndexOutOfBoundsException e) {
            httpStatus = HttpStatus.BAD_REQUEST;
            responseBody = showAlert("모든 필드를 입력하세요.", "http://localhost:8080/registration");
        }

        return new HttpResponse()
                .addStatus(httpStatus)
                .addHeader("Location", "/")
                .addHeader("Content-Length", String.valueOf(responseBody.length))
                .addHeader("Content-Type", "text/html;charset=UTF-8")
                .addBody(responseBody);
    }

    static HttpResponse loginUser(HttpRequest httpRequest)  {
        String body = new String(httpRequest.getBody());

        String[] bodyTokens = body.split("&");


        HttpResponse response = new HttpResponse();
        byte[] responseBody = new byte[0];

        try {
            String userId = bodyTokens[0].split("=")[1];
            String password = bodyTokens[1].split("=")[1];

            UserProcessor.loginUser(userId, password);
            String sid = Session.createSession(userId);

            response.addStatus(HttpStatus.FOUND)
                    .addHeader("Location", "/")
                    .addHeader("Set-Cookie", "sid=" + sid + "; Path=/");
        } catch (CustomException e) {
            responseBody = showAlert(e.getMessage(), "http://localhost:8080/login");
            response.addStatus(e.getHttpStatus())
                    .addBody(responseBody);
        } catch (ArrayIndexOutOfBoundsException e) {
            responseBody = showAlert("모든 필드를 입력하세요.", "http://localhost:8080/login");
            response.addStatus(HttpStatus.BAD_REQUEST)
                    .addBody(responseBody);
        }

        response.addHeader("Content-Length", String.valueOf(responseBody.length))
                .addHeader("Content-Type", "text/html;charset=UTF-8");
        return response;
    }
}
