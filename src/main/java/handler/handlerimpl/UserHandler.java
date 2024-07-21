package handler.handlerimpl;

import constant.HttpMethod;
import cookie.RedirectCookie;
import handler.Handler;
import model.MyTagDomain;
import model.User;
import repository.UserRepository;
import session.Session;
import util.DynamicFileBuilder;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static handler.HandlerManager.getBodyParams;
import static repository.DatabaseConnection.getConnection;

public class UserHandler {
    // 회원가입 handler
    public static Handler userCreateHanlder = (httpRequest, httpResponse) -> {
        Connection connection = getConnection();

        Map<String, String> bodyParams = getBodyParams(httpRequest);

        // User를 DB에 저장
        User user = new User(bodyParams);
        UserRepository.addUser(user, connection);

        // 302 응답 생성
        httpResponse.setRedirect("/");
    };

    public static Handler userListHandler = (httpRequest, httpResponse) -> {
        Connection connection = getConnection();

        Map<String, List<MyTagDomain>> model = new HashMap<>();

        // 쿠키에 있는 sessionId가 유효한지 검사
        if(httpRequest.getSessionId().isPresent()){
            String sessionId = httpRequest.getSessionId().get();
            String userId = Session.getUserId(sessionId);
            if(userId != null && UserRepository.userExists(userId, connection)){
                List<User> users = UserRepository.findAllByList(connection);
                List<MyTagDomain> userList = new ArrayList<>(users);
                model.put("userlist", userList);
                // 동적 index.html 반환
                DynamicFileBuilder.setHttpResponse(httpResponse, "/user/list.html", model);
                return;
            }
        }

        // 쿠키에 세션 정보가 없거나 유효하지 않으면 로그인 페이지로 이동
        // 로그인 성공 시, /user/list로 redirect 할 수 있도록 redirect cookie 설정
        RedirectCookie cookie = new RedirectCookie("/user/list");
        httpResponse.setCookie(cookie);
        httpResponse.setRedirect("/login/index.html");
    };
}
