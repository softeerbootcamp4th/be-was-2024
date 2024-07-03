package webserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileContentReader {
    private static final FileContentReader instance = new FileContentReader();

    public static FileContentReader getInstance() {
        return instance;
    }

    public byte[] readStaticResource(String uri) throws IOException {
        String path = "src/main/resources/static/";
        File file = new File(path + uri);

        // Not Found
        if (!file.exists() || !file.isFile()) {
            return "<h1>404 NOT FOUND</h1>".getBytes();
        }

        FileInputStream fis = null;
        byte[] byteArray = null;

        try {
            fis = new FileInputStream(file);
            byteArray = new byte[(int) file.length()];

            int bytesRead = 0;
            int offset = 0;
            while (offset < byteArray.length
                    && (bytesRead = fis.read(byteArray, offset, byteArray.length - offset)) >= 0) {
                offset += bytesRead;
            }

            if (offset < byteArray.length) {
                throw new IOException("Could not completely read the file " + file.getName());
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return byteArray;

//        StringBuilder contentBuilder = new StringBuilder();
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                contentBuilder.append(line).append("\n");
//            }
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + path);
//            e.printStackTrace();
//            return null;
//        }

//        return contentBuilder.toString().getBytes();
    }
}
