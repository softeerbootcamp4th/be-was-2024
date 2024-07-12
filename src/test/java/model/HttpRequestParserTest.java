package model;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.HttpRequest;
import webserver.HttpRequestParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HttpRequestParserTest {

    @Test
    @DisplayName("Testing HttpRequestParserTest parse header and body - Success case")
    public void testParseHttpRequestWithHeadersAndBody() throws IOException {
        // Given

        String httpRequest = "GET /index.html HTTP/1.1\r\n" +
                "Host: www.example.com\r\n" +
                "Content-Length: 10\r\n" +
                "\r\n" +
                "0123456789";

        // When

        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest parsedRequest = parser.parse(inputStream);
        Map<String, String> headers = parsedRequest.getHeaders();
        byte[] body = parsedRequest.getBody();

        // Then

        assertNotNull(parsedRequest);
        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/index.html", parsedRequest.getUrl());
        assertEquals(2, headers.size());
        assertEquals("www.example.com", headers.get("Host"));
        assertEquals("10", headers.get("Content-Length"));
        assertNotNull(body);
        assertEquals(10, body.length);
        assertArrayEquals("0123456789".getBytes(), body);
    }

    @Test
    @DisplayName("Testing HttpRequestParserTest parse empty body - Success case")
    public void testParseHttpRequestWithEmptyBody() throws IOException {
        // Given

        String httpRequest = "POST /submit HTTP/1.1\r\n" +
                "Host: www.example.com\r\n" +
                "Content-Length: 0\r\n" +
                "\r\n";

        // When

        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest parsedRequest = parser.parse(inputStream);
        Map<String, String> headers = parsedRequest.getHeaders();

        // Then

        assertNotNull(parsedRequest);
        assertEquals("POST", parsedRequest.getMethod());
        assertEquals("/submit", parsedRequest.getUrl());
        assertEquals(2, headers.size());
        assertEquals("www.example.com", headers.get("Host"));
        assertEquals("0", headers.get("Content-Length"));

        assertNotNull(parsedRequest.getBody());
    }

    @Test
    @DisplayName("Testing HttpRequestParserTest parse without body - Failure case")
    public void testParseHttpRequestWithoutBody() throws IOException {
        // Given

        String httpRequest = "GET /resource HTTP/1.1\r\n" +
                "Host: www.example.com\r\n" +
                "\r\n";

        // When

        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest parsedRequest = parser.parse(inputStream);
        Map<String, String> headers = parsedRequest.getHeaders();

        // Then

        assertNotNull(parsedRequest);
        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/resource", parsedRequest.getUrl());
        assertEquals(1, headers.size());
        assertEquals("www.example.com", headers.get("Host"));

        assertNotNull(parsedRequest.getBody());
    }

    @Test
    @DisplayName("Testing HttpRequestParserTest parse without headers - Failure case")
    public void testParseHttpRequestWithNoHeaders() throws IOException {
        // Given

        String httpRequest = "GET /index HTTP/1.1\r\n" +
                "\r\n";

        // When

        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());
        HttpRequestParser parser = new HttpRequestParser();
        HttpRequest parsedRequest = parser.parse(inputStream);
        Map<String, String> headers = parsedRequest.getHeaders();

        // Then

        assertNotNull(parsedRequest);
        assertEquals("GET", parsedRequest.getMethod());
        assertEquals("/index", parsedRequest.getUrl());
        assertEquals(0, headers.size());

        assertNotNull(parsedRequest.getBody());
    }

}
