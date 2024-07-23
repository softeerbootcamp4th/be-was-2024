package webserver.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.enums.HttpMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpRequestParserTest {

    private final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @ParameterizedTest
    @CsvSource({
            "GET /favicon.ico HTTP/1.1, GET, /favicon.ico, HTTP/1.1",
            "POST  /index.html   HTTP/1.1, POST, /index.html, HTTP/1.1",
            "GET /helloworld     HTTP/1.1, GET, /helloworld, HTTP/1.1"
    })
    @DisplayName("Request Line 파싱 테스트")
    void HttpRequestURIParserTest(String uri, String method, String path, String version) {
        // Given
        String requestFirstLine = uri;

        // When
        String[] requestLines = httpRequestParser.parseRequestFirstLine(requestFirstLine);

        // Then
        assertEquals(method, requestLines[0]);
        assertEquals(path, requestLines[1]);
        assertEquals(version, requestLines[2]);
    }

    @ParameterizedTest
    @CsvSource({
            "GET /favicon.icoHTTP/1.1",
            "POST/index.html   HTTP/1.1",
            "GET /helloworld, GET"
    })
    @DisplayName("Request Line 파싱 테스트 - 잘못된 Request Line")
    void HttpRequestURIParserWrongRequestLineTest(String uri) {
        // Given
        String requestFirstLine = uri;

        // When
        assertThrows(IllegalArgumentException.class, () -> httpRequestParser.parseRequestFirstLine(requestFirstLine));

    }

    @ParameterizedTest
    @CsvSource({
            "name=abc&age=20, name, abc, 2",
            "age=20&name=abc&email=abc@def , name, abc, 3"
    })
    @DisplayName("Query 파싱 테스트")
    void HTTPRequestUriQueryParserTest(String uri, String key, String value, int size) {
        // Given
        Map<String, String> queries;

        // When
        queries = httpRequestParser.parseQuery(uri);

        // Then
        assertEquals(value, queries.get(key));
        assertEquals(size, queries.size());
    }

    @ParameterizedTest
    @CsvSource({
            "name=abc&age:20",
            "age=20&name=abc&email="
    })
    @DisplayName("Query 파싱 테스트 - 잘못된 Query")
    void HTTPRequestUriQueryParserWrongQueryTest(String uri) {
        // Given

        // When
        assertThrows(IllegalArgumentException.class, () -> httpRequestParser.parseQuery(uri));
        // Then
    }

    @Test
    @DisplayName("HttpRequest 전문 파싱 테스트")
    void HTTPRequestUriQueryParserTest() throws IOException {
        // Given
        String httpRequest = "POST /api/data HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 16\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "\n" +
                "name=John&age=30";
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());


        // When
        MyHttpRequest myHttpRequest = httpRequestParser.parseRequest(inputStream);

        // Then
        assertEquals(HttpMethod.POST, myHttpRequest.getMethod());
        assertEquals("/api/data", myHttpRequest.getPath());
        assertEquals("HTTP/1.1", myHttpRequest.getVersion());

        assertEquals("localhost:8080", myHttpRequest.getHeaders().get("Host".toLowerCase()));
        assertEquals("keep-alive", myHttpRequest.getHeaders().get("Connection".toLowerCase()));
        assertEquals("16", myHttpRequest.getHeaders().get("Content-Length".toLowerCase()));
        assertEquals("application/x-www-form-urlencoded", myHttpRequest.getHeaders().get("Content-Type".toLowerCase()));

        assertEquals("name=John&age=30", new String(myHttpRequest.getBody()));

    }

    @Test
    @DisplayName("HttpRequest 전문 파싱 테스트")
    void HTTPRequestParserUpperCaseLowerCaseTest() throws IOException {
        // Given
        String httpRequest = "post /api/data Http/1.1\n" +
                "Host: localhost:8080\n" +
                "connection: keep-alive\n" +
                "CONTENT-LENGTH: 16\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "\n" +
                "name=John&age=30";
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());


        // When
        MyHttpRequest myHttpRequest = httpRequestParser.parseRequest(inputStream);

        // Then
        assertEquals(HttpMethod.POST, myHttpRequest.getMethod());
        assertEquals("/api/data", myHttpRequest.getPath());
        assertEquals("HTTP/1.1", myHttpRequest.getVersion());

        assertEquals("localhost:8080", myHttpRequest.getHeaders().get("host"));
        assertEquals("keep-alive", myHttpRequest.getHeaders().get("connection"));
        assertEquals("16", myHttpRequest.getHeaders().get("content-length"));
        assertEquals("application/x-www-form-urlencoded", myHttpRequest.getHeaders().get("content-type"));

        assertEquals("name=John&age=30", new String(myHttpRequest.getBody()));

    }


    @Test
    @DisplayName("HttpRequest 전문 파싱 테스트 - 잘못된 Request Line")
    void HTTPRequestUriQueryParserTestRequestLineError() throws IOException {
        // Given
        String httpRequest = "POST/api/dataHTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Content-Length: 16\n" +
                "Content-Type: application/x-www-form-urlencoded\n" +
                "\n" +
                "name=John&age=30";
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());


        // When
        assertThrows(IllegalArgumentException.class, () -> httpRequestParser.parseRequest(inputStream));
    }

    @Test
    @DisplayName("HttpRequest 전문 파싱 테스트 - 잘못된 Request header")
    void HTTPRequestUriQueryParserTestFail() throws IOException {
        // Given
        String httpRequest = "POST /api/data HTTP/1.1\n" +
                "Host = localhost:8080\n" +
                "Connection = keep-alive\n" +
                "Content-Length 16\n" +
                "Content-Type application/x-www-form-urlencoded\n" +
                "\n" +
                "name=John&age=30";
        InputStream inputStream = new ByteArrayInputStream(httpRequest.getBytes());


        // When
        assertThrows(IllegalArgumentException.class, () -> httpRequestParser.parseRequest(inputStream));
    }
}