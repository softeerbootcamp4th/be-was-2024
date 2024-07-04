package util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
class UtilsTest {

    @Test
    void getAllStrings() throws IOException {

        //given
        String input = "Hello, World!\nThis is a test.\nJava is fun.";

        byte[] inputBytes = input.getBytes();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        //when
        String allStrings = Utils.getAllStrings(inputStream);

        //then
        assertTrue(allStrings.equals(input));

    }

    @Test
    void getUrl() throws IOException {

        //given
        String request = "GET /index.html HTTP/1.1\n" +
                "Host: localhost:8080\n" +
                "Connection: keep-alive\n" +
                "Accept: */*\n";

        //when
        String url = Utils.getUrl(request);

        //then
        assertEquals(url, "index.html");

    }

    @Test
    void getFile() throws IOException {
        //HELP resources 폴더 안의 테스트 파일에 의존함.
        byte[] bytes = new byte[]{'t', 'e', 's', 't'};
        assertArrayEquals(bytes, Utils.getFile("/test/test.html"));
    }

    @Test
    void contentType() {

        //given
        String contentType = "text/html";

        //when
        String result = Utils.getContentType("html");

        //then
        assertEquals(contentType, result);

    }

}