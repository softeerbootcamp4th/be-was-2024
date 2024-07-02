import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpRequestParser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpRequestParserTest {

    @DisplayName("RequestLine 파싱")
    @Test
    void parseRequestLine() {
        // given
        String line = "GET /index.html HTTP/1.1";

        // when
        String[] requestLine = HttpRequestParser.parseRequestLine(line);

        // then
        assertEquals("GET", requestLine[0]);
        assertEquals("/index.html", requestLine[1]);
        assertEquals("HTTP/1.1", requestLine[2]);
    }

    @DisplayName("RequestLine 파싱 - NullPointerException")
    @Test
    void parseRequestLineError() {
        // given
        String line = null;

        // when & then
        assertThrows(NullPointerException.class, () -> HttpRequestParser.parseRequestLine(line));
    }
}
