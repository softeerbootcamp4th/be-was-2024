package webserver;

import webserver.http.MyHttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class FileContentReader {
    private static final FileContentReader instance = new FileContentReader();

    private FileContentReader() {
    }

    public static FileContentReader getInstance() {
        return instance;
    }

    public boolean isStaticResource(String uri) {
        String path = "src/main/resources/static";
        File file = new File(path + uri);
        return file.exists() && file.isFile();
    }

    public MyHttpResponse readStaticResource(String uri) throws IOException {
        String path = "src/main/resources/static";
        File file = new File(path + uri);

        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            byte[] byteArray = new byte[(int) file.length()];

            int bytesRead = 0;
            int offset = 0;
            while (offset < byteArray.length
                    && (bytesRead = fis.read(byteArray, offset, byteArray.length - offset)) >= 0) {
                offset += bytesRead;
            }

            if (offset < byteArray.length) {
                throw new IOException("Could not completely read the file " + file.getName());
            }

            String extension = uri.substring(uri.lastIndexOf(".") + 1);
            String contentType = ContentType.valueOf(extension).getContentType();

            return new MyHttpResponse(200, "OK", new HashMap<>() {
                {
                    put("Content-Type", contentType);
                    put("Content-Length", String.valueOf(byteArray.length));
                }
            }, byteArray);

        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


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
