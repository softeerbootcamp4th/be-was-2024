package webserver;

import ApiProcess.ApiProcess;
import db.Database;
import enums.HttpCode;
import enums.HttpMethod;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RequestParserTest {

    private static Logger logger = LoggerFactory.getLogger(RequestParserTest.class);
    ApiProcessManager apiProcessManager = ApiProcessManager.getInstance();

    @Test
    @DisplayName("정상적인 http request가 들어온 경우")
    void requestTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\nConnection: keep-alive\nHost: localhost:8080\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("http/1.1");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
    }

    @Test
    @DisplayName("request header 사이에 공백이 들어간 경우")
    void whiteSpaceTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\nConnection :    keep-alive\nHost: localhost:8080   \n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("http/1.1");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
    }

    @Test
    @DisplayName("request body가 application/x-www-form-data로 들어온 정상적인 경우")
    void requestBodyTest() throws IOException {
        // given
        String httpMessage = "POST /index.html http/1.1\r\n" +
                "Connection :    keep-alive\r\n" +
                "Host: localhost:8080\r\n" +
                "Content-Length: 46\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                "username=minjun&userId=minjun123&password=1234\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("http/1.1");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
        assertThat(request.getParameter("username")).isEqualTo("minjun");
        assertThat(request.getParameter("userId")).isEqualTo("minjun123");
        assertThat(request.getParameter("password")).isEqualTo("1234");
    }

    @Test
    @DisplayName("form 데이터 중 한글이 존재할 때")
    void formDataHanguelTest() throws IOException {
        // given
        String httpMessage = "POST /index.html http/1.1\r\n" +
                "Connection :    keep-alive\r\n" +
                "Host: localhost:8080\r\n" +
                "Content-Length: 95\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net\r\n";

        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getParameter("name")).isEqualTo("박재성");
        assertThat(request.getParameter("email")).isEqualTo("javajigi@slipp.net");
    }

    @Test
    @DisplayName("request body의 쿼리스트링 중 빈 문자가 들어온 경우")
    void requestBodyEmptyTest() throws IOException {
        // given
        String httpMessage = "POST /index.html http/1.1\r\n" +
                "Connection :    keep-alive\r\n" +
                "Host: localhost:8080\r\n" +
                "Content-Length: 46\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                "username=&userId=minjun123&password=1234\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("http/1.1");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
        assertThat(request.getParameter("username")).isNull();
        assertThat(request.getParameter("userId")).isEqualTo("minjun123");
        assertThat(request.getParameter("password")).isEqualTo("1234");
    }

    @Test
    @DisplayName("회원 등록 로직 테스트")
    void registerTest() throws IOException {
        // given
        String httpMessage = "POST /user/create http/1.1\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 70\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Host: localhost:8080\r\n\r\n" +
                "userId=minjun1234&name=minjun&password=1234&email=minjun%40naver.com\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());
        Request request = RequestParser.getRequestParser().getRequest(bis);
        Response response = new Response();
        String userId = request.getParameter("userId");
        ApiProcess apiProcess = apiProcessManager.getApiProcess(request.getPath(), request.getMethod());
        logger.debug("apiProcess={}", apiProcess.getClass());

        // when
        apiProcess.process(request, response);
        User user = Database.findUserById(userId);

        // then
        assertThat(request.getPath()).isEqualTo("/user/create");
        assertThat(userId).isEqualTo("minjun1234");
        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("minjun1234");
        assertThat(user.getEmail()).isEqualTo("minjun@naver.com");
        assertThat(user.getName()).isEqualTo("minjun");
        assertThat(user.getPassword()).isEqualTo("1234");
    }

    @Test
    @DisplayName("데이터가 부분적으로 들어왔을 때")
    void dataInsuffientTest() throws IOException {
        // given
        String httpMessage = "POST /user/create http/1.1\r\n" +
                "Connection: keep-alive\r\n" +
                "Content-Length: 70\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n" +
                "Host: localhost:8080\r\n\r\n" +
                "userId=minjun1234&password=1234&email=minjun%40naver.com\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());
        Request request = RequestParser.getRequestParser().getRequest(bis);
        Response response = new Response();
        String userId = request.getParameter("userId");
        ApiProcess apiProcess = apiProcessManager.getApiProcess(request.getPath(), request.getMethod());
        logger.debug("apiProcess={}", apiProcess.getClass());

        // when
        String fileName = apiProcess.process(request, response);

        // then
        assertThat(response.getHttpCode()).isEqualTo(HttpCode.BAD_REQUEST);
        assertThat(fileName).isEqualTo("error/400");
    }

    @Test
    @DisplayName("정상적인 쿠키가 들어왔을 때")
    void requestCookieTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\r\n" +
                "Cookie: price=10; minjun=20\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                "username=minjun&userId=minjun123&password=1234\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getCookie("price")).isEqualTo("10");
        assertThat(request.getCookie("minjun")).isEqualTo("20");
    }

    @Test
    @DisplayName("쿠키 값이 하나만 들어왔을 때")
    void requestOnlyOneCookieTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\r\n" +
                "Cookie: price=10\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                "username=minjun&userId=minjun123&password=1234\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getCookie("price")).isEqualTo("10");
    }

    @Test
    @DisplayName("빈 폼 데이터가 들어왔을 때")
    void emptyFormDataTest() throws IOException {
        // given
        String httpMessage = "POST /index.html http/1.1\r\n" +
                "Cookie: price=10\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getCookie("price")).isEqualTo("10");
    }

    @Test
    @DisplayName("쿠키의 key-value 사이의 구분자가 잘못되었을 때")
    void wrongKeyValueDelimeterTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\r\n" +
                "Cookie: price=10; name:minjun\r\n" +
                "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
                "username=minjun&userId=minjun123&password=1234\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when, then
        assertThatThrownBy(() -> {
            RequestParser.getRequestParser().getRequest(bis);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}