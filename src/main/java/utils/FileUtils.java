package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    public static boolean isExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isFile(String filePath) {
        File file = new File(filePath);
        return file.isFile();
    }

    public static byte[] readFileToBytes(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        }

        return bytes;
    }
}
