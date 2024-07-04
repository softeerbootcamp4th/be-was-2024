package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    @Test
    void requestTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\nConnection: keep-alive\nHost: localhost:8080\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when
        Request request = new HttpRequestParser().getRequest(bis);

        // then
        Assertions.assertThat(request.getMethod()).isEqualTo("GET");
        Assertions.assertThat(request.getPath()).isEqualTo("/index.html");
        Assertions.assertThat(request.getHttpVersion()).isEqualTo("http/1.1");
        Assertions.assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
        Assertions.assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
    }

    @Test
    void wrongRequestTest() throws IOException {
        // given
        String httpMessage = "GET /index.html http/1.1\nConnection: keep-alive: k\nHost: localhost:8080\n";
        ByteArrayInputStream bis = new ByteArrayInputStream(httpMessage.getBytes());

        // when, then
        Assertions.assertThatThrownBy(() -> {
            new HttpRequestParser().getRequest(bis);
        }).isInstanceOf(IllegalStateException.class);
    }
}