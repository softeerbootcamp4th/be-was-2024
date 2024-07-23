package webserver;

import utils.ResourceUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class MimeTypeTest {

    @Test
    @DisplayName("resource handler의 get mime 테스트. ico를 입력하면 해당하는 image/x-icon 반환하는지 체크")
    void test() {
        // given
        ResourceUtil resourceUtil = new ResourceUtil();

        // when
        String contentType = resourceUtil.getContentType("/back.ico");

        // then
        assertThat(contentType).isEqualTo("image/x-icon");
    }
}