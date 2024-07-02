package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class URLParserTest {
    URLParser urlParser = new URLParser();
    String line = "GET /index.html HTTP/1.1";

    @Test
    @DisplayName("get url 테스트")
    void testGetURL() {
        String url = urlParser.getGetURL(line);
        Assertions.assertThat(url).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("http 메소드 테스트")
    void testGetHttpMethod() {
        String method = urlParser.getHttpMethod(line);
        Assertions.assertThat(method).isEqualTo("GET");
    }
}