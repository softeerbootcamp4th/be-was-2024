package handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processor.UserProcessor;
import webserver.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

class SessionHandlerTest {
    @Test
    @DisplayName("로그인 요청을 보냈을 때 세션아이디가 만들어지고 response에 잘 전달되는지 테스트")
    void testLoginCreateSessionId() throws IOException {
        // given
        String query = "POST /user/create\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Content-Length: 54\r\n" +
                "\r\n" +
                "userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com";

        Request request = Request.from(new ByteArrayInputStream(query.getBytes()));

        UserProcessor userProcessor = new UserProcessor();
        userProcessor.createUser(request);

        // when
        String loginQuery = "POST /login\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Content-Length: 25\r\n" +
                "\r\n" +
                "userId=1234&password=asdf";

        // 로그인 요청
        Request loginRequest = Request.from(new ByteArrayInputStream(loginQuery.getBytes()));
        // 로그인 처리 및 세션 생성
        userProcessor.login(loginRequest);

        String sessionId = SessionHandler.getSessionId("1234");

        // then
        assertThat(sessionId).isEqualTo(SessionHandler.getSessionId(loginRequest.parseBody().get("userId")));
    }

    @Test
    @DisplayName("로그아웃 요청 시 세션 삭제 테스트")
    void testDeleteSession() throws IOException {
        // given
        String createUserQuery = "POST /user/create\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Content-Length: 54\r\n" +
                "\r\n" +
                "userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com";

        Request request = Request.from(new ByteArrayInputStream(createUserQuery.getBytes()));

        UserProcessor userProcessor = new UserProcessor();
        userProcessor.createUser(request);

        String loginQuery = "POST /login\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Content-Length: 25\r\n" +
                "\r\n" +
                "userId=1234&password=asdf";

        // 로그인 요청
        Request loginRequest = Request.from(new ByteArrayInputStream(loginQuery.getBytes()));
        // 로그인 처리 및 세션 생성
        userProcessor.login(loginRequest);

        String sessionId = SessionHandler.getSessionId("1234");
        System.out.println(sessionId);

        // when
        String logoutQuery = "GET /logout\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Cookie: SID=" + sessionId;

        // 로그아웃 요청
        Request logoutRequest = Request.from(new ByteArrayInputStream(logoutQuery.getBytes()));
        // 로그아웃 처리 및 세션 삭제
        String userId = logoutRequest.getSessionId();
        SessionHandler.deleteSession(userId);

        // then
        assertThat(SessionHandler.getSessionId(userId)).isEqualTo(null);
    }

    @Test
    @DisplayName("로그인 후 쿠키로 세션아이디를 보냈을 때 존재하는지 확인 테스트")
    void testVerifySession() throws IOException {
        // given
        String createUserQuery = "POST /user/create\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Content-Length: 54\r\n" +
                "\r\n" +
                "userId=1234&name=qwer&password=asdf&email=wdwionv@wanef.com";

        Request request = Request.from(new ByteArrayInputStream(createUserQuery.getBytes()));

        UserProcessor userProcessor = new UserProcessor();
        userProcessor.createUser(request);

        String loginQuery = "POST /login\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Content-Length: 25\r\n" +
                "\r\n" +
                "userId=1234&password=asdf";

        // 로그인 요청
        Request loginRequest = Request.from(new ByteArrayInputStream(loginQuery.getBytes()));
        // 로그인 처리 및 세션 생성
        userProcessor.login(loginRequest);

        String sessionId = SessionHandler.getSessionId("1234");

        // when
        String loginRequestWithSessionIdCookieQuery = "GET /login\r\n" +
                "Host: localhost:8080\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Connection:    keep-alive\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n" +
                "Content-Length: 25\r\n" +
                "Cookie: SID=" + sessionId;

        Request loginWithSessionIdCookieRequest = Request.from(new ByteArrayInputStream(loginRequestWithSessionIdCookieQuery.getBytes()));

        System.out.println(sessionId);
        System.out.println(loginWithSessionIdCookieRequest.getSessionId());

        // then
        assertThat(SessionHandler.verifySessionId(loginWithSessionIdCookieRequest.getSessionId())).isEqualTo(true);
    }
}