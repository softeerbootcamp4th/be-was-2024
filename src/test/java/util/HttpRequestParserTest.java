package util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

class HttpRequestParserTest {

    @Test
    void testParse() throws IOException {
        String requestString =
                "GET /index.html?param=value HTTP/1.1\r\n" +
                        "Host: localhost:8080\r\n" +
                        "User-Agent: Mozilla/5.0\r\n" +
                        "\r\n";

        BufferedReader reader = new BufferedReader(new StringReader(requestString));
        HttpRequestParser parser = new HttpRequestParser();

        HttpRequest request = parser.parse(reader);

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getUrl()).isEqualTo("/index.html?param=value");
        assertThat(request.getPath()).isEqualTo("/index.html");
        assertThat(request.getHttpVersion()).isEqualTo("HTTP/1.1");
        assertThat(request.getHeader("Host")).isEqualTo("localhost:8080");
        assertThat(request.getQueryParams()).containsEntry("param", "value");
    }
}