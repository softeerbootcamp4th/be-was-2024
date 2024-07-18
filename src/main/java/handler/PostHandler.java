package handler;

import db.ArticleDatabase;
import db.SessionDatabase;
import db.UserDatabase;
import http.*;
import model.User;
import processer.UserProcessor;
import util.exception.CustomException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static util.Constants.*;
import static util.TemplateEngine.showAlert;
import static util.Utils.*;

public class PostHandler {
    private static ArticleDatabase articleDatabase = new ArticleDatabase();
    private static UserDatabase userDatabase = new UserDatabase();

    static HttpResponse createUser(HttpRequest httpRequest) {
        String body = new String(httpRequest.getBody().get(0).getBody());
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
        } catch (ArrayIndexOutOfBoundsException e) {
            return showAlert("모든 필드를 입력하세요.", PATH_HOST + PATH_REGISTRATION);
        } catch (CustomException e) {
            return showAlert(e.getMessage(), PATH_HOST + PATH_REGISTRATION);
        }

        return new HttpResponse()
                .addStatus(httpStatus)
                .addHeader(LOCATION, PATH_ROOT)
                .addHeader(CONTENT_LENGTH, String.valueOf(responseBody.length))
                .addHeader(CONTENT_TYPE, TEXT_HTML)
                .addBody(responseBody);
    }

    static HttpResponse loginUser(HttpRequest httpRequest) {
        String body = new String(httpRequest.getBody().get(0).getBody());

        String[] bodyTokens = body.split(REG_AMP);
        HttpResponse response = new HttpResponse();
        byte[] responseBody = new byte[0];

        try {
            String userId = bodyTokens[0].split(REG_EQ)[1];
            String password = bodyTokens[1].split(REG_EQ)[1];

            UserProcessor.loginUser(userId, password);
            String sid = SessionDatabase.createSession(userId);

            response.addStatus(HttpStatus.FOUND)
                    .addHeader(LOCATION, PATH_ROOT)
                    .addHeader(SET_COOKIE, "sid=" + sid + "; Path=/");
        } catch (CustomException e) {
            return showAlert(e.getMessage(), PATH_HOST + PATH_LOGIN);
        } catch (ArrayIndexOutOfBoundsException e) {
            return showAlert("모든 필드를 입력하세요.", PATH_HOST + PATH_LOGIN);
        }

        response.addHeader(CONTENT_LENGTH, String.valueOf(responseBody.length))
                .addHeader(CONTENT_TYPE, TEXT_HTML)
                .addBody(new byte[0]);
        return response;
    }

    static HttpResponse postArticle(HttpRequest httpRequest) throws CustomException {
        String cookie = httpRequest.getHeaders(COOKIE);
        HashMap<String, String> parsedCookie = cookieParsing(cookie);
        String sid = parsedCookie.get(SID);
        if (sid == null) throw new CustomException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        String userId = SessionDatabase.getUser(sid);
        Optional<User> user = userDatabase.findUserById(userId);

        ArrayList<RequestBody> parts = httpRequest.getBody();
        byte[] image = new byte[0];
        String text = null;
        for (RequestBody part : parts) {
            RequestMultipartBody multipartBody = (RequestMultipartBody) part;
            HashMap<String, String> headers = multipartBody.getHeaders();
            byte[] body = multipartBody.getBody();

            String contentType = headers.get("content-type");

            for (String s : headers.keySet()) {
                System.out.println("s = " + s);
                System.out.println("headers.get(s) = " + headers.get(s));
            }


            if (contentType == null) {
                text = new String(body);
            } else image = body;
        }

        if (image.length == 0) throw new CustomException(HttpStatus.BAD_REQUEST, "이미지를 등록해주세요.");
        if (text.equals("\r\n")) throw new CustomException(HttpStatus.BAD_REQUEST, "본문을 작성해주세요.");

        articleDatabase.createArticle(user.get().getName(), text, image);

        return new HttpResponse()
                .addStatus(HttpStatus.FOUND)
                .addHeader(LOCATION, PATH_ROOT)
                .addBody(new byte[0]);
    }

}
