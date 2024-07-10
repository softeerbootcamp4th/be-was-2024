package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
    public static byte[] readAllBytesFromFile(File file) throws IOException {
        byte[] bytes;
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            long fileLength = file.length();
            if (fileLength > Integer.MAX_VALUE) {
                throw new IOException("File is too large to read into a byte array");
            }
            bytes = new byte[(int) fileLength];
            int read = bufferedInputStream.read(bytes);
            if (read < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }
        }
        return bytes;
    }
}
