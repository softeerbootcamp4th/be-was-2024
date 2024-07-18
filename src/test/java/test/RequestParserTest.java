package test;

import http.HttpMethod;
import http.StartLine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.RequestParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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

        assertStartLine(expectedStartline, actualStartLine);
        assertStartLine(expectedStartline, actualStartLine_lowerCase);
        assertStartLine(expectedStartline, actualStartLine_mixedCase);
        assertStartLine(expectedStartline, actualStartLine_whiteSpace);
    }

    @Test
    @DisplayName("Headers 파싱 테스트")
    void testHeadersParser() throws IOException {
        StringBuilder headerString = new StringBuilder();
        headerString.append("lower-case: lower-case-value\r\n");
        headerString.append("UPPER-CASE: UPPER-CASE-VALUE\r\n");
        headerString.append("mIXeD-caSE: MixEd-CASe-VAlue\r\n");
        headerString.append("optional-whitespace:OWS\r\n");
        headerString.append("required-whitespace: RWS\r\n");
        headerString.append("bad-whitespace   :    BWS\r\n\r\n");

        HashMap<String, String> headers = requestParser.getHeaders(getInputStream(headerString.toString()), new StringBuilder());

        assertEquals("lower-case-value", headers.get("lower-case"));
        assertEquals("UPPER-CASE-VALUE", headers.get("upper-case"));
        assertEquals("MixEd-CASe-VAlue", headers.get("mixed-case"));
        assertEquals("OWS", headers.get("optional-whitespace"));
        assertEquals("RWS", headers.get("required-whitespace"));
        assertEquals("BWS", headers.get("bad-whitespace"));
    }
}
