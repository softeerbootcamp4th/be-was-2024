package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtil {
    public static byte[] readAllBytesFromFile(File file) throws IOException {
        byte[] bytes;
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            bytes = fileInputStream.readAllBytes();
        }
        return bytes;
    }
}
