package test;

import http.HttpMethod;
import http.StartLine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RequestParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestParserTest {
    RequestParser requestParser = new RequestParser();

    InputStream getInputStream(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }

    void assertStartLine(StartLine expectedStartLine, StartLine actualStartLine) {
        assertEquals(expectedStartLine.getHttpMethod(), actualStartLine.getHttpMethod());
        assertEquals(expectedStartLine.getRequestUrl(), actualStartLine.getRequestUrl());
        assertEquals(expectedStartLine.getVersion(), actualStartLine.getVersion());
    }

    @Test
    @DisplayName("StartLine 파싱 테스트")
    void testStartLineParser(){
        StartLine expectedStartline = new StartLine(HttpMethod.GET, "/index.html", "HTTP/1.1");
        String getStartLine = "GET /index.html HTTP/1.1\r\n";
        String getStartLine_lowerCase = "get /index.html HTTP/1.1\r\n";
        String getStartLine_mixedCase = "gEt /index.html HTTP/1.1\r\n";
        String getStartLine_whiteSpace = "gEt   /index.html  HTTP/1.1\r\n";

        StartLine actualStartLine = requestParser.getStartLine(getInputStream(getStartLine), new StringBuilder());
        StartLine actualStartLine_lowerCase = requestParser.getStartLine(getInputStream(getStartLine_lowerCase), new StringBuilder());
        StartLine actualStartLine_mixedCase = requestParser.getStartLine(getInputStream(getStartLine_mixedCase), new StringBuilder());
        StartLine actualStartLine_whiteSpace = requestParser.getStartLine(getInputStream(getStartLine_whiteSpace), new StringBuilder());

        assertStartLine(actualStartLine, actualStartLine);
        assertStartLine(actualStartLine, actualStartLine_lowerCase);
        assertStartLine(actualStartLine, actualStartLine_mixedCase);
        assertStartLine(actualStartLine, actualStartLine_whiteSpace);

    }
}
