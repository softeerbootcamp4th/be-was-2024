package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestParserTest {

    String line = "GET /index.html HTTP/1.1";

    @DisplayName("RequestLine을 파싱하여 총 3가지 요소를 반환한다.")
    @Test
    void parseRequestLine() {
        // when
        String[] requestLine = HttpRequestParser.parseRequestLine(line);

        // then
        assertThat(requestLine).containsExactly("GET", "/index.html", "HTTP/1.1");
    }

    @DisplayName("RequestLine을 파싱하여 RequestMethod만을 반환한다.")
    @Test
    void parseRequestMethod() {
        // when
        String requestMethod = HttpRequestParser.parseRequestMethod(line);

        // then
        assertThat(requestMethod).isEqualTo("GET");
    }

    @DisplayName("RequestLine을 파싱하여 RequestURI만을 반환한다.")
    @Test
    void parseRequestURI() {
        // when
        String requestURI = HttpRequestParser.parseRequestURI(line);

        // then
        assertThat(requestURI).isEqualTo("/index.html");
    }
}
