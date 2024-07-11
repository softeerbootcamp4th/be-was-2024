package test;

import http.HttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static handler.GetHandler.serveDynamicFile;
import static handler.GetHandler.serveStaticFile;
import static org.junit.jupiter.api.Assertions.*;
import static util.Constants.*;

public class GetHandlerTest {

    @Test
    @DisplayName("루트 페이지 반환 테스트")
    void 루트페이지_반환_테스트() throws IOException {
        HttpResponse response = serveStaticFile("/index.html");
        File file = new File(STATIC_PATH + FILE_INDEX);

        String statusLine = response.getStatusLine();
        String contentType = response.getHeader(CONTENT_TYPE);

        assertEquals(response.getBody().length, file.length());
        assertEquals(statusLine, "HTTP/1.1 200 Ok\r\n");
        assertEquals(contentType, "text/html");
    }

    @Test
    @DisplayName("404 반환 테스트")
    void NOT_FOUND_에러_반환_테스트() throws IOException {
        HttpResponse response = serveStaticFile("/not_exists");
        File file = new File(STATIC_PATH + PATH_ERROR + FILE_NOT_FOUND);

        String statusLine = response.getStatusLine();

        assertEquals(response.getBody().length, file.length());
        assertEquals(statusLine, "HTTP/1.1 404 Not found\r\n");
    }

    @Test
    @DisplayName("동적 파일 반환 테스트")
    void 동적_파일_반환_테스트() throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("userName", "testUserName");
        HttpResponse response = serveDynamicFile("/main/index.html", data);

        String body = new String(response.getBody());
        boolean contains_parameters = body.contains("{{userName}}");
        boolean contains_username = body.contains("testUserName");

        assertFalse(contains_parameters);
        assertTrue(contains_username);
    }
}
