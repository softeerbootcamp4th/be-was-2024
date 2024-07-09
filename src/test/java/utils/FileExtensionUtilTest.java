package utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileExtensionUtilTest {
    @Test
    @DisplayName("확장자 있으면 확장자 반환")
    void getFileExtension_returnExtensionIfExist() {
        String input = "index.html";
        String expectedExtension = "html";

        String extension = FileExtensionUtil.getFileExtension(input);
        Assertions.assertThat(extension).isEqualTo(expectedExtension);
    }

    @Test
    @DisplayName("확장자 없으면 null 반환")
    void getFileExtension_returnNullIfExtensionNotExist() {
        String input = "TEST";

        String extension = FileExtensionUtil.getFileExtension(input);
        Assertions.assertThat(extension).isNull();
    }

    @Test
    @DisplayName(". 이 여러개인 경우에도 정확히 확장자만 반환")
    void getFileExtension_returnExtensionEvenIfManyDots() {
        String input = "global.module.css";
        String expectedExtension = "css";

        String extension = FileExtensionUtil.getFileExtension(input);
        Assertions.assertThat(extension).isEqualTo(expectedExtension);
    }
}