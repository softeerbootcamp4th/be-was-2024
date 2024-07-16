package test;

import db.Database;
import db.Session;
import http.HttpMethod;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static handler.Router.requestMapping;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.Constants.*;
import static util.Utils.cookieParsing;

public class PostHandlerTest {

    @Test
    @DisplayName("회원가입 테스트")
    void POST_회원가입_테스트() throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.setStartLine(HttpMethod.POST, PATH_USER + PATH_CREATE, "HTTP/1.1");

        String params = "userId=testId&name=testName&password=testPassword&email=testEmail";
        httpRequest.setBody(params.getBytes());

        requestMapping(httpRequest);

        User user = Database.findUserById("testId");
        assertEquals(user.getUserId(), "testId");
        assertEquals(user.getName(), "testName");
        assertEquals(user.getPassword(), "testPassword");
        assertEquals(user.getEmail(), "testEmail");
    }


    @Test
    @DisplayName("로그인 성공 테스트")
    void 로그인시_쿠키_발급_테스트() throws IOException {
        HttpRequest registrationRequest = new HttpRequest();
        registrationRequest.setStartLine(HttpMethod.POST, PATH_USER + PATH_CREATE, "HTTP/1.1");

        String registrationParams = "userId=testId&name=testName&password=testPassword&email=testEmail";
        registrationRequest.setBody(registrationParams.getBytes());

        requestMapping(registrationRequest);


        HttpRequest loginRequest = new HttpRequest();
        loginRequest.setStartLine(HttpMethod.POST, PATH_USER + PATH_LOGIN, "HTTP/1.1");

        String loginParams = "userId=testId&password=testPassword";
        loginRequest.setBody(loginParams.getBytes());

        HttpResponse response = requestMapping(loginRequest);

        String cookie = response.getHeader("Set-Cookie");
        HashMap<String, String> parsedCookie = cookieParsing(cookie);

        String sid = parsedCookie.get("sid");

        String userId = Session.getUser(sid);
        User user = Database.findUserById(userId);
        ;

        assertEquals(user.getUserId(), "testId");
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 가입 정보가 없을 때")
    void 가입하지_않은_아이디() throws IOException {
        HttpRequest loginRequest = new HttpRequest();
        loginRequest.setStartLine(HttpMethod.POST, PATH_USER + PATH_LOGIN, "HTTP/1.1");

        String loginParams = "userId=notRegisteredId&password=testPassword";
        loginRequest.setBody(loginParams.getBytes());
        HttpResponse response = requestMapping(loginRequest);

        assertEquals(response.getStatusLine(), "HTTP/1.1 400 Bad request\r\n");
    }

    @Test
    @DisplayName("로그인 실패 테스트 - 파라미터 없음")
    void 필드값_오류() throws IOException {
        HttpRequest registrationRequest = new HttpRequest();
        registrationRequest.setStartLine(HttpMethod.POST, PATH_USER + PATH_CREATE, "HTTP/1.1");

        String registrationParams = "userId=testId&name=testName&password=testPassword&email=testEmail";
        registrationRequest.setBody(registrationParams.getBytes());

        requestMapping(registrationRequest);


        HttpRequest loginRequest = new HttpRequest();
        loginRequest.setStartLine(HttpMethod.POST, PATH_USER + PATH_LOGIN, "HTTP/1.1");

        String loginParams = "userId=testId&password=";
        loginRequest.setBody(loginParams.getBytes());

        HttpResponse response = requestMapping(loginRequest);
        assertEquals(response.getStatusLine(), "HTTP/1.1 400 Bad request\r\n");
    }
    @Test
    @DisplayName("로그인 실패 테스트 - 비밀번호 오류")
    void 비밀번호_오류() throws IOException {
        HttpRequest registrationRequest = new HttpRequest();
        registrationRequest.setStartLine(HttpMethod.POST, PATH_USER + PATH_CREATE, "HTTP/1.1");

        String registrationParams = "userId=testId&name=testName&password=testPassword&email=testEmail";
        registrationRequest.setBody(registrationParams.getBytes());

        requestMapping(registrationRequest);


        HttpRequest loginRequest = new HttpRequest();
        loginRequest.setStartLine(HttpMethod.POST, PATH_USER + PATH_LOGIN, "HTTP/1.1");

        String loginParams = "userId=testId&password=wrongPassword";
        loginRequest.setBody(loginParams.getBytes());

        HttpResponse response = requestMapping(loginRequest);
        assertEquals(response.getStatusLine(), "HTTP/1.1 401 Unauthorized\r\n");
    }
}
