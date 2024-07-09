package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static webserver.FileHandler.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHandlerTest {

    @Test
    @DisplayName("Testing FileHandler readFileToByteArray method - Success Case")
    public void testReadFileToByteArray() throws IOException {
        // Given
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();
        String content = "Test!";
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(content.getBytes());
        }

        // When
        byte[] expectedContent = content.getBytes();
        byte[] actualContent = readFileToByteArray(tempFile);

        // Then
        assertArrayEquals(expectedContent, actualContent);
    }

    @Test
    @DisplayName("Testing FileHandler determineContentType method - Success Case")
    public void testDetermineContentType() {
        // Given

        // When

        // Then
        assertEquals("text/html", determineContentType("index.html"));
        assertEquals("text/css", determineContentType("styles.css"));
        assertEquals("application/javascript", determineContentType("app.js"));
        assertEquals("application/json", determineContentType("data.json"));
        assertEquals("image/png", determineContentType("image.png"));
        assertEquals("image/jpeg", determineContentType("photo.jpg"));
        assertEquals("image/jpeg", determineContentType("photo.jpeg"));
        assertEquals("image/svg+xml", determineContentType("vector.svg"));
        assertEquals("image/x-icon", determineContentType("favicon.ico"));
    }

    @Test
    @DisplayName("Testing FileHandler readFileToByteArray method - Failure Case")
    public void testReadFileToByteArrayFailure() throws IOException {
        // Given
        File tempFile = File.createTempFile("testFile", ".txt");
        tempFile.deleteOnExit();
        String content = "Test!";
        writeFileContent(tempFile, content);

        // When
        byte[] expectedContent = content.getBytes();
        byte[] actualContent = readFileToByteArray(tempFile);

        // Then
        assertArrayEquals(expectedContent, actualContent);

        // Failure case: Non-existent file
        File nonExistentFile = new File("nonexistent.txt");
        assertThrows(IOException.class, () -> readFileToByteArray(nonExistentFile));
    }

    @Test
    @DisplayName("Testing FileHandler determineContentType method - Failure Case")
    public void testDetermineContentTypeFailure() {
        // Given

        // When

        // Then
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

        // Failure case: Unknown file extension
        assertEquals("application/octet-stream", determineContentType("unknown.xyz"));
    }

    // 테스트를 위한 메서드 작성
    private void writeFileContent(File file, String content) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
        }
    }
}
