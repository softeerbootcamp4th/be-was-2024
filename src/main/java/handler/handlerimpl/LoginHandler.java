package handler.handlerimpl;

import constant.HttpMethod;
import cookie.RedirectCookie;
import cookie.SessionCookie;
import exception.InvalidHttpRequestException;
import handler.Handler;
import model.User;
import repository.UserRepository;
import session.Session;

import java.sql.Connection;
import java.util.Map;
import static handler.HandlerManager.getBodyParams;
import static repository.DatabaseConnection.getConnection;

/**
 * 로그인 관련 작업을 처리하는 클래스
 */
public class LoginHandler {

    // 로그인 처리 handler
    public static Handler userLoginHandler = (httpRequest, httpResponse) -> {
        Connection connection = getConnection();

        Map<String, String> bodyParams = getBodyParams(httpRequest);

        String userId = bodyParams.get("userId");
        String password = bodyParams.get("password");

        try {
            User.getValueFromMap(bodyParams, "userId");
            User.getValueFromMap(bodyParams, "password");
        } catch (InvalidHttpRequestException e) {
            httpResponse.setRedirect("/login/login_failed.html");
            return;
        }

        if (UserRepository.userExists(bodyParams.get("userId"), connection)) {

            User user = UserRepository.findByUserId(userId, connection);

            // 로그인 성공 시, /index.html로 redirect
            if (user.getPassword().equals(password)) {
                String sessionId = Session.createSession(userId);
                httpResponse.setCookie(new SessionCookie(sessionId));

                // 로그인을 하기 전에 유저 리스트 버튼을 눌렀다면
                // /user/list로 리다이렉트 하고, redirect 쿠키를 삭제
                if (httpRequest.getRedirectUrl().isPresent() &&
                        httpRequest.getRedirectUrl().get().equals("/user/list")) {

                    RedirectCookie cookie = new RedirectCookie("/user/list");
                    cookie.setMaxAge(0);
                    httpResponse.setCookie(cookie);
                    httpResponse.setRedirect("/user/list");
                } else if (httpRequest.getRedirectUrl().isPresent() &&
                        httpRequest.getRedirectUrl().get().equals("/post/form")) {

                    RedirectCookie cookie = new RedirectCookie("/post/form");
                    cookie.setMaxAge(0);
                    httpResponse.setCookie(cookie);
                    httpResponse.setRedirect("/post/form.html");
                } else
                    httpResponse.setRedirect("/");
            }

        }

        // 로그인 실패 시, /login/failed.html로 redirect
        httpResponse.setRedirect("/login/login_failed.html");
    };

    // 로그아웃 처리 handler
    public static Handler logoutHandler = (httpRequest, httpResponse) -> {

        if(httpRequest.getSessionId().isPresent()){
            String sessionId = httpRequest.getSessionId().get();
            Session.deleteSession(sessionId);

            // 세션 쿠키 삭제
            SessionCookie sessionCookie = new SessionCookie(sessionId);
            sessionCookie.setMaxAge(0);
            httpResponse.setCookie(sessionCookie);
        }

        httpResponse.setRedirect("/");

    };

}
