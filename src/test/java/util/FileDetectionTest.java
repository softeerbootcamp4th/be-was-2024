package util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;


class FileDetectionTest {

    @DisplayName("해당 path에 존재하는 파일이 디렉토리인지 파일인지 판단하는 메소드 테스트")
    @Test
    void 파일판단Test()
    {
        //given
        String path = "src/main/resources/static";

        //when
        path = FileDetection.getPath(path);

        //Then
        assertThat(path).isEqualTo("src/main/resources/static/index.html");
    }
}