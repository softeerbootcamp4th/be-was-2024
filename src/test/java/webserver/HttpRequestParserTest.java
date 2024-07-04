package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestParserTest {
    @Test
    @DisplayName("getRequestString 테스트")
    public void testParse() throws IOException {
        //given
        InputStream inputStream = new FileInputStream("src/test/resources/test.txt");

        //when
        String requestString = HttpRequestParser.getRequestString(inputStream);

        //then
        assertThat(requestString).isEqualTo("file test\n");
    }
}
