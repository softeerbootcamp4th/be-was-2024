package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class RequestLineTest {

    private final String url = "GET /index.html HTTP/1.1";
    private RequestLine requestLine = new RequestLine(url);

    @DisplayName("path테스트")
    @Test
    public void path테스트()
    {
        Assertions.assertEquals("src/main/resources/static/index.html",requestLine.getPath());
    }

    @DisplayName("method테스트")
    @Test
    public void method테스트() {
        Assertions.assertEquals("GET",requestLine.getMethod());
    }

}