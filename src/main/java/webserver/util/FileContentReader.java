package webserver.util;

import webserver.http.MyHttpResponse;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileContentReader {
    private static final FileContentReader instance = new FileContentReader();

    private final ClassLoader classLoader = getClass().getClassLoader();

    public static final String STATIC_RESOURCE = "static";

    private FileContentReader() {
    }

    public static FileContentReader getInstance() {
        return instance;
    }

    public boolean isStaticResource(String uri) {
        URL resourcePath = classLoader.getResource(STATIC_RESOURCE + uri);
        return resourcePath != null && !Files.isDirectory(Paths.get(resourcePath.getPath()));
    }

    public MyHttpResponse readStaticResource(String uri, MyHttpResponse response) throws IOException {
        String resourcePath = STATIC_RESOURCE + uri;

        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] byteArray = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(byteArray)) != -1) {
                outputStream.write(byteArray, 0, bytesRead);
            }

            response.setBody(outputStream.toByteArray());
            response.addContentType(uri);
            return response;

        }
    }

    public byte[] readStaticResourceByAbsolute(String path) throws IOException {
        try (InputStream inputStream = new FileInputStream(new File(path));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] byteArray = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(byteArray)) != -1) {
                outputStream.write(byteArray, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }
}
