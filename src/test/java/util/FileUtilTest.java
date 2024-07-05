package util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class FileUtilTest {
    @Test
    @DisplayName("readAllByteFromFile 테스트")
    public void testReadAllByteFromFile() throws IOException {
        //given
        File file = new File("src/test/resources/test.txt");

        //when
        byte[] bytes = FileUtil.readAllBytesFromFile(file);

        //then
        assertThat(new String(bytes)).isEqualTo("file test");
    }
}
