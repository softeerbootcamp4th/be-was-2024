package webserver;

import handler.ResourceHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TypeTest {

    @Test
    @DisplayName("resource handler get mime 테스트")
    void test() {
        // given
        ResourceHandler resourceHandler = new ResourceHandler();

        // when
        String contentType = resourceHandler.getContentType("/back.ico");

        // then
        assertThat(contentType).isEqualTo("image/x-icon");
    }
}