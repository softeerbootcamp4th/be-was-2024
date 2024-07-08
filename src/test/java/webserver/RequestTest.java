package webserver;

import ApiProcess.ApiProcess;
import db.Database;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.RequestParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class RequestTest {

    @Test
    void requestTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\nConnection: keep-alive\nHost: localhost:8080\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("http/1.1");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
    }

    @Test
    void whiteSpaceTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\nConnection :    keep-alive\nHost: localhost:8080   \n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = RequestParser.getRequestParser().getRequest(bis);

        // then
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("http/1.1");
        assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
    }
    @Test
    void registerTest() throws IOException {
        // given
        String httpMessage = "GET /user/create?userId=adsf&name=asf&email=sdf&password=sadf http/1.1\nConnection: keep-alive\nHost: localhost:8080\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());
        Request request = RequestParser.getRequestParser().getRequest(bis);
        Response response = new Response();
        String userId = request.getParameter("userId");
        ApiProcessManager apiProcessManager = new ApiProcessManager();
        ApiProcess apiProcess = apiProcessManager.getApiProcess(request.getPath());

        // when
        String process = apiProcess.process(request, response);
        User user= Database.findUserById(userId);

        // then
        assertThat(user.getEmail()).isEqualTo("sdf");
        assertThat(user.getName()).isEqualTo("asf");
        assertThat(user.getPassword()).isEqualTo("sadf");
    }
}