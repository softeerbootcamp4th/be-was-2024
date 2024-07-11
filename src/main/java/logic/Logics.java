package logic;

import db.Database;
import model.HttpRequest;
import model.HttpResponse;
import model.User;
import model.enums.HttpMethod;
import model.enums.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestConverter;
import webserver.RequestHandler;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.constant.StringConstants.*;

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
        Database.addUser(user);
        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);
        //TODO : body를 리턴하지 않아도 되는가
    }

    public static HttpResponse login(HttpRequest httpRequest) throws IOException {
        if (!httpRequest.getHttpMethod().equals(HttpMethod.POST)) {
            throw new RuntimeException("Invalid method");
        }

        //아이디와 비밀번호를 매칭
        //TODO :get 하기전 getOrThrow 처리

        ///header
        Map<String, String> headers = new HashMap<>();

        // body
        Map<String, String> bodyToMap = HttpRequestConverter.bodyToMap(httpRequest.getBody());

        String userId = bodyToMap.get(USER_ID);
        String password = bodyToMap.get(PASSWORD);

        User user = Database.findUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (userId.equals(user.getUserId()) && password.equals(user.getPassword())) {
            headers.put("Location", "/index.html");
            logger.info("userId : "+userId+"Login successful");
        } else {
            headers.put("Location", "/login_failed.html");
            logger.info("userId : "+userId+"Login failed");
        }

        return HttpResponse.of(PROTOCOL_VERSION, HttpStatus.SEE_OTHER, headers, new byte[0]);
    }
}
