package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReadUtil {
    public static byte[] read(String filePath) {
        File targetFile = new File(filePath);

        byte[] buffer;
        if(targetFile.exists() && !targetFile.isDirectory()) {
            int bodyLength = (int)targetFile.length();

            buffer = new byte[bodyLength];
            try(FileInputStream fis = new FileInputStream(targetFile)) {
                fis.read(buffer, 0, buffer.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            buffer = new byte[0];
        }

        return buffer;
    }
}
