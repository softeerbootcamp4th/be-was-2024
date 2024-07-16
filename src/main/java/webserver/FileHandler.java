package webserver;

import webserver.enumPackage.ContentType;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileHandler {

    public static byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) { //BufferedInputStream
            byte[] buffer = new byte[baos.size()+1];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    public static String determineContentType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return ContentType.OCTET_STREAM.getMimeType();
        }
        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        return ContentType.fromExtension(extension);
    }
}
