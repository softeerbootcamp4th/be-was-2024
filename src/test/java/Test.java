import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


public class Test {
    @org.junit.jupiter.api.Test
    void ClassLoader_파일_내용_읽기() throws IOException {
        // Given
        ClassLoader classLoader = getClass().getClassLoader();
        String resourcePath = "static/main.css";

        //When
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] byteArray = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(byteArray)) != -1) {
                outputStream.write(byteArray, 0, bytesRead);
            }

            byte[] combinedByteArray = outputStream.toByteArray();

            //Then
            assertNotNull(combinedByteArray);
        }
    }

    @org.junit.jupiter.api.Test
    void isFile_파일() {
        // Given
        ClassLoader classLoader = getClass().getClassLoader();
        String uri = "static/index.html";

        // When
        URL resource = classLoader.getResource(uri);
        Boolean a = resource != null && !Files.isDirectory(Paths.get(resource.getPath()));

        // Then
        assertNotNull(resource);
        assertEquals(true, a);

    }

    @org.junit.jupiter.api.Test
    void isFile_404() {
        // Given
        ClassLoader classLoader = getClass().getClassLoader();
        String uri = "static/abc";

        // When
        URL resource = classLoader.getResource(uri);
        Boolean a = resource != null && !Files.isDirectory(Paths.get(resource.getPath()));

        // Then
        assertNull(resource);
        assertEquals(false, a);

    }

    @org.junit.jupiter.api.Test
    void isFile_Dir() {
        // Given
        ClassLoader classLoader = getClass().getClassLoader();
        String uri = "static/registration";

        // When
        URL resource = classLoader.getResource(uri);
        Boolean a = resource != null && !Files.isDirectory(Paths.get(resource.getPath()));

        // Then
        assertNotNull(resource);
        assertEquals(false, a);

    }
}
