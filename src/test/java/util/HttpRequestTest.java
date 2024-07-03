package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.StringReader;

public class HttpRequestTest {

    @Test
    public void testParseRequestLine() throws Exception {
        String requestText = "GET /index.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(requestText));
        HttpRequest request = new HttpRequest(reader);

        assertEquals("GET", request.getMethod());
        assertEquals("/index.html", request.getUrl());
        assertEquals("HTTP/1.1", request.getHttpVersion());
    }

    @Test
    public void testParseHeaders() throws Exception {
        String requestText = "GET /index.html HTTP/1.1\r\nHost: localhost\r\nUser-Agent: JUnit\r\n\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(requestText));
        HttpRequest request = new HttpRequest(reader);

        assertEquals("localhost", request.getHeader("Host"));
        assertEquals("JUnit", request.getHeader("User-Agent"));
    }

    @Test
    public void testSetContentType() throws Exception {
        String requestText = "GET /index.html HTTP/1.1\r\nHost: localhost\r\n\r\n";
        BufferedReader reader = new BufferedReader(new StringReader(requestText));
        HttpRequest request = new HttpRequest(reader);

        assertEquals("text/html", request.getContentType());

        String requestTextCss = "GET /style.css HTTP/1.1\r\nHost: localhost\r\n\r\n";
        reader = new BufferedReader(new StringReader(requestTextCss));
        request = new HttpRequest(reader);

        assertEquals("text/css", request.getContentType());

        String requestTextJs = "GET /script.js HTTP/1.1\r\nHost: localhost\r\n\r\n";
        reader = new BufferedReader(new StringReader(requestTextJs));
        request = new HttpRequest(reader);

        assertEquals("application/javascript", request.getContentType());
    }
}
