package model;

import org.junit.jupiter.api.Test;
import static webserver.FileHandler.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileHandlerTest {

    @Test
    public void testReadFileToByteArray() throws IOException {
        // 임시 파일 생성 및 데이터 작성
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();
        String content = "Test!";
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(content.getBytes());
        }

        // FileHandler를 사용하여 파일을 읽기
        byte[] expectedContent = content.getBytes();
        byte[] actualContent = readFileToByteArray(tempFile);

        // 읽은 내용이 동일한지 확인
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    public void testDetermineContentType() {
        assertEquals("text/html", determineContentType("index.html"));
        assertEquals("text/css", determineContentType("styles.css"));
        assertEquals("application/javascript", determineContentType("app.js"));
        assertEquals("application/json", determineContentType("data.json"));
        assertEquals("image/png", determineContentType("image.png"));
        assertEquals("image/jpeg", determineContentType("photo.jpg"));
        assertEquals("image/jpeg", determineContentType("photo.jpeg"));
        assertEquals("image/svg+xml", determineContentType("vector.svg"));
        assertEquals("image/x-icon", determineContentType("favicon.ico"));
        assertEquals("application/octet-stream", determineContentType("file.unknown"));
    }
}
