package webserver;

import org.junit.jupiter.api.Test;
import util.Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpHeaderParserTest {

    @Test
    void parseHeadersOnlyHeaderCase() throws IOException {

        //given
        String request = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n";

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost:8080");
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");

        //when
        Map<String, String> parsedHeaders = HttpHeaderParser.parseHeaders(request);

        //then
        assertEquals(headers, parsedHeaders);

    }

    @Test
    void parseHeadersWithBodyCase() throws IOException {

        //given
        String request = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n\nbody";

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "localhost:8080");
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");

        //when
        Map<String, String> parsedHeaders = HttpHeaderParser.parseHeaders(request);

        //then
        assertEquals(headers, parsedHeaders);

    }

    @Test
    void parseHeaderName() throws IOException {

        //given
        String header = "Host: localhost:8080";

        //when
        String name = HttpHeaderParser.parseHeaderName(header);

        //then
        assertEquals("Host", name);

    }

    @Test
    void parseHeaderValue(){

        //given
        String header = "Host: localhost:8080";

        //when
        String value = HttpHeaderParser.parseHeaderValue(header);

        //then
        assertEquals("localhost:8080", value);

    }

}