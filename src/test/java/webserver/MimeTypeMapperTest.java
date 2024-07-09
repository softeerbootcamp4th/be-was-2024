package webserver;

import enums.MimeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.MimeTypeMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MimeTypeMapperTest {

    @Test
    @DisplayName("올바른 확장자가 들어왔을 때")
    void mimeTypeMapperCorrectTest() {
       // given
       MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
       String ext = ".css";

       // when
       MimeType mimeType = mimeTypeMapper.getMimeType(ext);

       // then
       assertThat(mimeType).isEqualTo(MimeType.CSS);
    }

    @Test
    @DisplayName("mimeType이 존재하지 않는 파일 확장자가 들어왔을 때")
    void mimeTypeMapperIncorrectTest() {
        // given
        MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
        String ext = ".cpp";

        // when, then
        assertThatThrownBy(() -> {
            mimeTypeMapper.getMimeType(ext);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}