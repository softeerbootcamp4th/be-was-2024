package webserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TYPETest {
    @Test
    void test() {
        ResourceHandler resourceHandler = new ResourceHandler();

        String contentType = resourceHandler.getContentType("GET /back.ico HTTP/1.0");
        assertEquals("image/x-icon", contentType);
    }
}