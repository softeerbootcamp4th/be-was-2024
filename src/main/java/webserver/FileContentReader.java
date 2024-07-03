package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileContentReader {

    public static byte[] readStaticResource(String uri) {
        String path = "src/main/resources/static/";
        File file = new File(path + uri);

        // Not Found
        if (!file.exists() || !file.isFile()) {
            return "<h1>404 NOT FOUND</h1>".getBytes();
        }

        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + path);
            e.printStackTrace();
            return null;
        }

        return contentBuilder.toString().getBytes();
    }
}
