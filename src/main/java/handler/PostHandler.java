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

/**
 * POST 요청을 처리하는 핸들러입니다.
 */
public class PostHandler {
    private static final ArticleDatabase articleDatabase = new ArticleDatabase();
    private static final UserDatabase userDatabase = new UserDatabase();

    /**
     * 유저를 생성하는 메서드입니다.
     * @param httpRequest Http 요청을 담은 HttpRequest 객체입니다.
     * @return HttpResponse 객체를 통해 회원가입에 성공한다면 메인 페이지로 리다이렉트, 실패한다면 Alert 창을 반환합니다.
     * @throws ArrayIndexOutOfBoundsException 모든 필드가 입력되지 않았을 때 반환됩니다.
     * @throws CustomException 이미 존재하는 아이디를 사용한다면 반환됩니다.
     */
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

    /**
     * 로그인 메서드입니다.
     * @param httpRequest Http 요청을 담은 HttpRequest 객체입니다.
     * @return HttpResponse를 통해 로그인에 성공한다면 메인 페이지로 리다이렉트, 쿠키에 세션 아이디를 포함하여 반환합니다.
     */

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

    /**
     * 게시글 업로드 메서드입니다.
     * @param httpRequest Http 요청을 담은 HttpRequest 객체입니다.
     * @return 게시글 업로드 성공 시 HttpResponse를 통해 루트 페이지로 리다이렉트합니다.
     * @throws CustomException 본문이나 이미지가 없다면 반환됩니다.
     */
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
            if (contentType == null) {
                text = new String(body);
            } else image = body;
        }

        if (image.length <= 2) throw new CustomException(HttpStatus.BAD_REQUEST, "이미지를 등록해주세요.");
        if (text.equals("\r\n")) throw new CustomException(HttpStatus.BAD_REQUEST, "본문을 작성해주세요.");

        articleDatabase.createArticle(user.get().getName(), text, image);

        return new HttpResponse()
                .addStatus(HttpStatus.FOUND)
                .addHeader(LOCATION, PATH_ROOT)
                .addBody(new byte[0]);
    }

}