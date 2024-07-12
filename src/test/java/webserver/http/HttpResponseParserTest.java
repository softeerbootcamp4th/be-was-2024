package webserver.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.FileContentReader;
import webserver.mapping.MappingHandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpResponseParserTest {
    private HttpResponseParser httpResponseParser;
    private FileContentReader fileContentReader;
    private MappingHandler mappingHandler;

    @BeforeEach
    void setUp() {
        fileContentReader = mock(FileContentReader.class);
        mappingHandler = mock(MappingHandler.class);

        httpResponseParser = new HttpResponseParser(fileContentReader, mappingHandler);
    }

    @Test
    @DisplayName("sendResponse 테스트")
    void sendResponseTest() {
        // Given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        MyHttpResponse httpResponse = new MyHttpResponse(200, "OK", Map.of("Content-Type", "text/html"), "Hello, World!".getBytes());

        //When
        httpResponseParser.sendResponse(dos, httpResponse);
        assertEquals("HTTP/1.1 200 OK \r\nContent-Type: text/html\r\n\r\nHello, World!\r\n", byteArrayOutputStream.toString());
    }

    @Test
    @DisplayName("Static Resource 요청 테스트")
    void test2() throws IOException {
        // Given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        MyHttpRequest httpRequest = new MyHttpRequest("GET", "/", null, "HTTP/1.1", Map.of("Host", "localhost:8080"), null);
        MyHttpResponse httpResponse = new MyHttpResponse(200, "OK", Map.of("Content-Type", "text/html"), "Hello, World!".getBytes());

        when(fileContentReader.isStaticResource(anyString())).thenReturn(true);
        when(fileContentReader.readStaticResource(anyString())).thenReturn(httpResponse);

        // When

        httpResponseParser.parseResponse(dos, httpRequest);

        // Then
        assertEquals("HTTP/1.1 200 OK \r\nContent-Type: text/html\r\n\r\nHello, World!\r\n", byteArrayOutputStream.toString());
    }

    @Test
    @DisplayName("Mapper 요청 테스트")
    void test() throws IOException {
        // Given
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(byteArrayOutputStream);

        MyHttpRequest httpRequest = new MyHttpRequest("GET", "/", null, "HTTP/1.1", Map.of("Host", "localhost:8080"), null);
        MyHttpResponse httpResponse = new MyHttpResponse(200, "OK", Map.of("Content-Type", "text/html"), "Hello, World!".getBytes());

        when(fileContentReader.isStaticResource(any())).thenReturn(false);
        when(mappingHandler.mapping(any())).thenReturn(httpResponse);

        // When
        httpResponseParser.parseResponse(dos, httpRequest);

        // Then
        assertEquals("HTTP/1.1 200 OK \r\nContent-Type: text/html\r\n\r\nHello, World!\r\n", byteArrayOutputStream.toString());
    }
}