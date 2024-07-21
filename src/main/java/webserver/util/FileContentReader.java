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

    public boolean isUploadedResource(String uri) {
        File file = new File(uri.substring(1));
        return uri.startsWith("/src/main/resources/upload/") && file.exists();
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

    public String readStaticResourceToString(String path) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(STATIC_RESOURCE + path)));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }

    public MyHttpResponse readUploadedResource(String uri, MyHttpResponse response) {
        File file = new File(uri.substring(1));
        byte[] fileContent = new byte[(int) file.length()];

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setBody(fileContent);
        response.addContentType(uri);

        return response;
    }
}
