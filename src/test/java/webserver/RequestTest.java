package webserver;

import ApiProcess.ApiProcess;
import db.Database;
import enums.HttpMethod;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.RequestParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTest {

    private static Logger logger = LoggerFactory.getLogger(RequestTest.class);

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
        String httpMessage = "POST /user/create?userId=adsf&name=asf&email=sdf&password=sadf http/1.1\r\n" +
                "Connection: keep-alive\r\n" +
                "Host: localhost:8080\r\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());
        Request request = RequestParser.getRequestParser().getRequest(bis);
        Response response = new Response();
        String userId = request.getParameter("userId");
        ApiProcessManager apiProcessManager = new ApiProcessManager();
        ApiProcess apiProcess = apiProcessManager.getApiProcess(request.getPath(), request.getMethod());

        // when
        String process = apiProcess.process(request, response);
        User user= Database.findUserById(userId);

        // then
        assertThat(user.getEmail()).isEqualTo("sdf");
        assertThat(user.getName()).isEqualTo("asf");
        assertThat(user.getPassword()).isEqualTo("sadf");
    }
}