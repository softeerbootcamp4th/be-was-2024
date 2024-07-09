package routehandler.route.auth;

import db.Database;
import http.MyHttpHeaders;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.enums.HttpStatusType;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SignInHandlerTest {
    // 유저 설정
    @BeforeAll
    static void setUp() {
        User user = new User("id", "password", null, null);
        Database.addUser(user);
    }

    @Test
    @DisplayName("로그인 정보가 일치하면 redirect")
    void redirectIfLoginSuccess() {
        String requestLine = "POST /auth/signin HTTP/1.1";
        MyHttpHeaders headers = new MyHttpHeaders();
        byte[] body = "userId=id&password=password".getBytes();

        MyHttpRequest request = new MyHttpRequest(requestLine, headers, body);
        MyHttpResponse response = new MyHttpResponse(request.getVersion());
        SignInHandler handler = new SignInHandler();

        handler.handle(request, response);

        Assertions.assertThat(response.getStatusType()).isEqualTo(HttpStatusType.FOUND);
    }

    @ParameterizedTest
    @MethodSource("loginFailProvider")
    @DisplayName("로그인 정보가 일치하지 않으면 401")
    void loginAgainIfLoginFail(byte[] body) {
        String requestLine = "POST /auth/signin HTTP/1.1";
        MyHttpHeaders headers = new MyHttpHeaders();

        MyHttpRequest request = new MyHttpRequest(requestLine, headers, body);
        MyHttpResponse response = new MyHttpResponse(request.getVersion());
        SignInHandler handler = new SignInHandler();

        handler.handle(request, response);

        Assertions.assertThat(response.getStatusType()).isEqualTo(HttpStatusType.UNAUTHORIZED);
    }

    private static Stream<Arguments> loginFailProvider() {
        return Stream.of(
                Arguments.of((Object) "userId=id&password=wrong".getBytes()),
                Arguments.of((Object) "userId=id&password=".getBytes()),
                Arguments.of((Object) "userId=&password=wrong".getBytes())
        );
    }
}