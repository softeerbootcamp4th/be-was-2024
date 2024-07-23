package handler.handlerimpl;

import constant.HttpMethod;
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
import static repository.DatabaseConnection.getConnection;

/**
 * 기본 화면을 보여주는 handler를 관리하는 클래스
 */
public class MainHandler {

    public static Handler mainHandler = (httpRequest, httpResponse) -> {
        Connection connection = getConnection();

        Map<String, List<MyTagDomain>> model = new HashMap<>();

        // 쿠키에 있는 sessionId가 유효한지 검사
        if(httpRequest.getSessionId().isPresent()){
            String sessionId = httpRequest.getSessionId().get();
            String userId = Session.getUserId(sessionId);
            if(userId != null && UserRepository.userExists(userId, connection)){
                User user = UserRepository.findByUserId(userId, connection);
                List<User> userList = new ArrayList<>();
                userList.add(user);
                model.put("login", new ArrayList<>(userList));
                // 동적 index.html 반환
                DynamicFileBuilder.setHttpResponse(httpResponse, "/index.html", model);
                return;
            }
        }

        // 쿠키에 세션 정보가 없거나 유효하지 않으면 빈 model 전달
        DynamicFileBuilder.setHttpResponse(httpResponse, "/index.html", model);
    };
}
