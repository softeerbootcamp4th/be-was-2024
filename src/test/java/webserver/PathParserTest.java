package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.PathParser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PathParserTest {

    PathParser fileParser = PathParser.getInstance();

    @Test
    @DisplayName("파일 이름이 정상적으로 들어갔을 때")
    void extExtractTest() {
        String fileName = "/index.html";
        String ext = fileParser.fileExtExtract(fileName);
        assertThat(ext).isEqualTo(".html");
    }

    @Test
    @DisplayName("파일 이름이 비정상적인 경우")
    void extExtractIncorrectTest() {
       String fileName = "asdfajjjdmfk";
       assertThatThrownBy(() -> {
           fileParser.fileExtExtract(fileName);
       }).isInstanceOf(IllegalArgumentException.class);
    }
}