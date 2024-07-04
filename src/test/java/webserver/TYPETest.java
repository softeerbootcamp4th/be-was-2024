package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TYPETest {

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