package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MimeTypeMapperTest {

    @Test
    @DisplayName("올바른 확장자가 들어왔을 때")
    void mimeTypeMapperCorrectTest() {
       MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
       String ext = ".css";
       MimeType mimeType = mimeTypeMapper.getMimeType(ext);
       assertThat(mimeType).isEqualTo(MimeType.CSS);
    }

    @Test
    @DisplayName("mimeType이 존재하지 않는 파일 확장자가 들어왔을 때")
    void mimeTypeMapperIncorrectTest() {
        MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
        String ext = ".cpp";
        assertThatThrownBy(() -> {
            mimeTypeMapper.getMimeType(ext);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}