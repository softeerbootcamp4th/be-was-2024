package webserver;

import db.Database;
import enums.Status;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Cookie;
import utils.Handler;
import utils.HttpRequestParser;
import utils.HttpResponseHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestHandlerTest {

    private static final String CRLF = "\r\n";

    @Test
    @DisplayName("GET 유저 생성 테스트")
    public void getAddUserTest() throws IOException {
        // given
        String queryParameter = "userId=123&password=123&name=123&email=123@gmail.com";
        String httpRequest =
                "GET /user/create?" + queryParameter + " HTTP/1.1" + CRLF +
                        "Host: localhost:8080" + CRLF +
                        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36" + CRLF +
                        "Accept: application/json" + CRLF +
                        "Connection: keep-alive" + CRLF +
                        CRLF;

        // when
        InputStream in = stringToInputStream(httpRequest);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HttpRequestParser httpRequestParser = new HttpRequestParser(in);
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

        Router router = new Router();
        Handler handler = router.getHandler(httpRequestParser);
        handler.handle(httpRequestParser, httpResponseHandler);

        // then
        Status status = httpResponseHandler.getStatus();
        assertThat(status).isEqualTo(Status.NOT_FOUND);
    }

    @Test
    @DisplayName("POST 유저 생성 테스트")
    public void postAddUserTest() throws IOException {
        // given
        String body = "userId=123&password=123&name=123&email=123@gmail.com";
        String httpRequest =
                "POST /user/create HTTP/1.1" + CRLF +
                "Host: localhost:8080" + CRLF +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36" + CRLF +
                "Accept: application/json" + CRLF +
                "Connection: keep-alive" + CRLF +
                "Content-Type: application/x-www-form-urlencoded" + CRLF +
                "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + CRLF +
                CRLF +
                body;

        InputStream in = stringToInputStream(httpRequest);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // when
        HttpRequestParser httpRequestParser = new HttpRequestParser(in);
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

        Router router = new Router();
        Handler handler = router.getHandler(httpRequestParser);
        handler.handle(httpRequestParser, httpResponseHandler);

        // then
        User user = Database.findUserById("123");
        assertThat(user.getUserId()).isEqualTo("123");
        assertThat(user.getName()).isEqualTo("123");
        assertThat(user.getPassword()).isEqualTo("123");
        assertThat(user.getEmail()).isEqualTo("123@gmail.com");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws IOException {
        // given
        User user = new User("123", "123", "123", "123@gmail.com");
        Database.addUser(user);

        // when
        String body = "userId=123&password=123";
        String request =
                "POST /login HTTP/1.1" + CRLF +
                        "Host: localhost:8080" + CRLF +
                        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36" + CRLF +
                        "Accept: application/json" + CRLF +
                        "Connection: keep-alive" + CRLF +
                        "Content-Type: application/x-www-form-urlencoded" + CRLF +
                        "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + CRLF +
                        CRLF +
                        body;

        InputStream in = stringToInputStream(request);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HttpRequestParser httpRequestParser = new HttpRequestParser(in);
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

        Router router = new Router();
        Handler handler = router.getHandler(httpRequestParser);
        handler.handle(httpRequestParser, httpResponseHandler);

        // then
        Map<String, String> responseHeadersMap = httpResponseHandler.getResponseHeadersMap();
        List<Cookie> cookieList = httpResponseHandler.getCookieList();
        String sessionId = SessionManager.getSession(user);
        Status status = httpResponseHandler.getStatus();

        Cookie sessionIdCookie = null;
        for (Cookie cookie : cookieList) {
            if (cookie.getName().equals("sid")) {
                sessionIdCookie = cookie;
                break;
            }
        }

        assertThat(status).isEqualTo(Status.FOUND);
        assertThat(sessionIdCookie).isNotNull();
        assertThat(sessionIdCookie.getValue()).isEqualTo(sessionId);
        assertThat(sessionIdCookie.getPath()).isEqualTo("/");
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws IOException {
        // given
        User user = new User("123", "123", "123", "123@gmail.com");
        Database.addUser(user);

        // when
        String body = "userId=12&password=123";
        String request =
                "POST /login HTTP/1.1" + CRLF +
                        "Host: localhost:8080" + CRLF +
                        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36" + CRLF +
                        "Accept: application/json" + CRLF +
                        "Connection: keep-alive" + CRLF +
                        "Content-Type: application/x-www-form-urlencoded" + CRLF +
                        "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + CRLF +
                        CRLF +
                        body;

        InputStream in = stringToInputStream(request);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HttpRequestParser httpRequestParser = new HttpRequestParser(in);
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

        Router router = new Router();
        Handler handler = router.getHandler(httpRequestParser);
        handler.handle(httpRequestParser, httpResponseHandler);

        // then
        Status status = httpResponseHandler.getStatus();
        assertThat(status).isEqualTo(Status.UNAUTHORIZED);
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void logoutTest() throws IOException {
        // given
        User user = new User("123", "123", "123", "123@gmail.com");
        Database.addUser(user);

        String session = SessionManager.createSession(user);

        // when
        String body = "userId=123&password=123";
        String request =
                "POST /logout HTTP/1.1" + CRLF +
                        "Host: localhost:8080" + CRLF +
                        "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36" + CRLF +
                        "Cookie: sid=" + session + CRLF +
                        "Accept: application/json" + CRLF +
                        "Connection: keep-alive" + CRLF +
                        "Content-Type: application/x-www-form-urlencoded" + CRLF +
                        "Content-Length: " + body.getBytes(StandardCharsets.UTF_8).length + CRLF +
                        CRLF +
                        body;

        InputStream in = stringToInputStream(request);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        HttpRequestParser httpRequestParser = new HttpRequestParser(in);
        HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

        Router router = new Router();
        Handler handler = router.getHandler(httpRequestParser);
        handler.handle(httpRequestParser, httpResponseHandler);

        // then
        Status status = httpResponseHandler.getStatus();
        List<Cookie> cookieList = httpResponseHandler.getCookieList();

        Cookie sessionIdCookie = null;
        for (Cookie cookie : cookieList) {
            if (cookie.getName().equals("sid")) {
                sessionIdCookie = cookie;
                break;
            }
        }
        assertThat(status).isEqualTo(Status.FOUND);
        assertThat(sessionIdCookie).isNotNull();
        assertThat(sessionIdCookie.getMaxAge()).isEqualTo(0);
        assertThat(sessionIdCookie.getValue()).isEqualTo(session);
        assertThat(sessionIdCookie.getPath()).isEqualTo("/");

    }

    private InputStream stringToInputStream(String httpRequest) {
        // 문자열을 바이트 배열로 변환
        byte[] httpRequestBytes = httpRequest.getBytes(StandardCharsets.UTF_8);

        // 바이트 배열을 InputStream으로 변환
        return new ByteArrayInputStream(httpRequestBytes);
    }

    private void request() {

    }

}