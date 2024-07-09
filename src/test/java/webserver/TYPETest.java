package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TYPETest {
    @Test
    @DisplayName("resource handler get mime 테스트")
    void test() {
        ResourceHandler resourceHandler = new ResourceHandler();

        String contentType = resourceHandler.getContentType("/back.ico");
        assertEquals("image/x-icon", contentType);
    }
}