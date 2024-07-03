package webserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

public class FileContentReaderTest {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @ParameterizedTest
    @CsvSource({"/favicon.ico", "/index.html"})
    @DisplayName("파일 읽기 성공")
    void FileReaderTestSuccess(String uri) throws IOException {
        // When
        byte[] content = fileContentReader.readStaticResource(uri);

        // Then
        Assertions.assertNotEquals(content, null);
    }

    @ParameterizedTest
    @ValueSource(strings = "/hello")
    @DisplayName("파일 읽기 실패")
    void FileReaderTestNotFound(String uri) throws IOException {
        // Given
        byte[] message = "<h1>404 NOT FOUND</h1>".getBytes();

        // When
        byte[] content = fileContentReader.readStaticResource(uri);

        // Then
        Assertions.assertNotEquals(content, message);
    }
}