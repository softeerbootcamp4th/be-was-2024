package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class HttpRequestParserTest {
    @Test
    @DisplayName("url 파싱 테스트")
    public void urlParsingTest() throws IOException {
        // given
        String httpRequest = "GET /api/users?userId=123 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\n" +
                "Accept: application/json\n" +
                "Connection: keep-alive\n";

        // when
        HttpRequestParser httpRequestParser = new HttpRequestParser(stringToInputStream(httpRequest));

        // then
        assertThat(httpRequestParser.getUrl()).isEqualTo("/api/users?userId=123");
    }

    @Test
    @DisplayName("path 파싱 테스트")
    public void pathParsingTest() throws IOException {
        // given
        String httpRequest = "GET /api/users?userId=123 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\n" +
                "Accept: application/json\n" +
                "Connection: keep-alive\n";
        // when
        HttpRequestParser httpRequestParser = new HttpRequestParser(stringToInputStream(httpRequest));

        // then
        assertThat(httpRequestParser.getPath()).isEqualTo("/api/users");
    }

    @Test
    @DisplayName("header 파싱 테스트")
    public void headerParsingTest() throws IOException {
        // given
        String httpRequest = "GET /api/users?userId=123 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\n" +
                "Accept: application/json\n" +
                "Connection: keep-alive\n";
        // when
        HttpRequestParser httpRequestParser = new HttpRequestParser(stringToInputStream(httpRequest));
        Map<String, String> headersMap = httpRequestParser.getRequestHeadersMap();

        // then
        assertThat(headersMap.get("Accept")).isEqualTo("application/json");
    }

    @Test
    @DisplayName("queryParameter 파싱 테스트")
    public void queryParameterParsingTest() throws IOException {
        // given
        String httpRequest = "GET /api/users?userId=123 HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\n" +
                "Accept: application/json\n" +
                "Connection: keep-alive\n";
        // when
        HttpRequestParser httpRequestParser = new HttpRequestParser(stringToInputStream(httpRequest));
        Map<String, String> queryParametersMap = httpRequestParser.getQueryParametersMap();

        // then
        assertThat(queryParametersMap.get("userId")).isEqualTo("123");
    }

    @Test
    @DisplayName("extension 파싱 테스트")
    public void extensionParsingTest() throws IOException {
        // given
        String httpRequest = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\n" +
                "Accept: application/json\n" +
                "Connection: keep-alive\n";
        // when
        HttpRequestParser httpRequestParser = new HttpRequestParser(stringToInputStream(httpRequest));

        // then
        assertThat(httpRequestParser.getExtension()).isEqualTo("html");
    }

    public InputStream stringToInputStream(String httpRequest) {
        // 문자열을 바이트 배열로 변환
        byte[] httpRequestBytes = httpRequest.getBytes(StandardCharsets.UTF_8);

        // 바이트 배열을 InputStream으로 변환
        return new ByteArrayInputStream(httpRequestBytes);
    }
}
