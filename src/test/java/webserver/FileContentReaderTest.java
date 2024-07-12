package webserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webserver.http.MyHttpResponse;

import java.io.IOException;

public class FileContentReaderTest {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();


    @ParameterizedTest
    @CsvSource({"/favicon.ico", "/index.html"})
    @DisplayName("Static Resource 파일 읽기, 성공")
    void readStaticResourceFile(String uri) throws IOException {
        // When
        boolean isStaticResource = fileContentReader.isStaticResource(uri);
        MyHttpResponse response = fileContentReader.readStaticResource(uri);

        // Then
        Assertions.assertTrue(isStaticResource);
        Assertions.assertNotEquals(response.getBody(), null);
    }

    @ParameterizedTest
    @CsvSource({"/registration", "/abc"})
    @DisplayName("Static Resource 파일 여부 확인, Not File")
    void isStaticResourceTestNotFile(String uri) throws IOException {
        // Given

        // When
        boolean isStaticResource = fileContentReader.isStaticResource(uri);

        // Then
        Assertions.assertFalse(isStaticResource);
    }
}
