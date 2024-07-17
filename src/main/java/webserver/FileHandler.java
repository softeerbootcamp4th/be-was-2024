package webserver;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import webserver.enumPackage.ContentType;

import java.io.*;
import java.util.List;

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
