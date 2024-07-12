package logic;

import db.SessionDatabase;
import db.UserDatabase;
import dto.HttpRequest;
import dto.HttpResponse;
import model.Session;
import model.User;
import dto.enums.HttpMethod;
import dto.enums.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static util.constant.StringConstants.*;

// 알맞은 HttpRequest 에 대해 로직을 처리하고 HttpResponse 를 반환
public class Logics {
    private static final Logger logger = LoggerFactory.getLogger(Logics.class);

    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String EMAIL = "email";

    public static HttpResponse create(HttpRequest httpRequest) throws IOException {
        if (!httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
            throw new RuntimeException("Invalid method");
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", "/index.html");

        // body
        Map<String, String> bodyToMap = HttpRequestConverter.bodyToMap(httpRequest.getBody());

        String userId = bodyToMap.get(USER_ID);
        String password = bodyToMap.get(PASSWORD);
        String name = bodyToMap.get(NAME);
        String email = bodyToMap.get(EMAIL);

        User user = new User(userId, password, name, email);
        UserDatabase.addUser(user);
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);
        //TODO : body를 리턴하지 않아도 되는가
    }

    public static HttpResponse login(HttpRequest httpRequest) throws IOException {
        if (!httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
            throw new RuntimeException("Invalid method");
        }

        ///header
        Map<String, String> headers = new HashMap<>();

        // body
        Map<String, String> bodyToMap = HttpRequestConverter.bodyToMap(httpRequest.getBody());
        String userId = bodyToMap.get(USER_ID);
        String password = bodyToMap.get(PASSWORD);

        User user = UserDatabase.findUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (userId.equals(user.getUserId()) && password.equals(user.getPassword())) {
            headers.put("Location", "/index.html");
            logger.info("userId : "+userId+"Login successful");

            Session session = new Session(UUID.randomUUID().toString(),userId,EXPIRE_TIME);
            SessionDatabase.addSession(session);

            //TODO : TOHeaderString 메서드로 만들기
            headers.put("Set-Cookie", SessionDatabase.convertSessionIdToHeaderString(session.getSessionId()));

        } else {
            headers.put("Location", "/login_failed.html");
            logger.info("userId : "+userId+"Login failed");
        }

        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);
    }
    public static HttpResponse logout(HttpRequest httpRequest) throws IOException {
        if (!httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
            throw new RuntimeException("Invalid method");
        }

        String session = httpRequest.getSessionOrNull().orElse(null);
        if (session!=null) {
            SessionDatabase.deleteSession(session);
            logger.info(SessionDatabase.getLogoutString(session));
        }

        ///header
        Map<String, String> headers = new HashMap<>();

        headers.put("Location", "/index.html");

        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);
    }
}
