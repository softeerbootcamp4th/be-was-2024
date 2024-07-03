package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpRequestObjectTest {

    @DisplayName("from 정적 팩토리 메서드로 RequestLine을 파싱 후 HttpRequestObject를 생성한다.")
    @Test
    void from() {
        // given
        String requestLine = "GET /index.html HTTP/1.1";

        // when
        HttpRequestObject httpRequestObject = HttpRequestObject.from(requestLine);

        // then
        assertEquals("GET", httpRequestObject.getRequestMethod());
        assertEquals("/index.html", httpRequestObject.getRequestURI());
        assertEquals("HTTP/1.1", httpRequestObject.getHttpVersion());
    }
}
