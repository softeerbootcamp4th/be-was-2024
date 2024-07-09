package webserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import webserver.http.MyHttpResponse;

import java.io.IOException;

public class FileContentReaderTest {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();


    @ParameterizedTest
    @CsvSource({"/favicon.ico", "/index.html"})
    @DisplayName("파일 읽기 성공")
    void FileReaderTestSuccess(String uri) throws IOException {
        // When
        MyHttpResponse response = fileContentReader.readStaticResource(uri);

        // Then
        Assertions.assertNotEquals(response.getBody(), null);
        //temp 변경
    }

    @ParameterizedTest
    @ValueSource(strings = "/hello")
    @DisplayName("파일 읽기 실패")
    void FileReaderTestNotFound(String uri) throws IOException {
        // Given

        // When
        MyHttpResponse response = fileContentReader.readStaticResource(uri);

        // Then
        Assertions.assertNull(response);
    }
    
}
