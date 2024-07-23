package utils;

import config.AppConfig;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriteUtil {
    public static void writeToLocal(String filename, byte[] content) {
        String _filename = filename.startsWith("/") ? filename.substring(1) : filename;
        try(FileOutputStream fos = new FileOutputStream(AppConfig.FILE_SRC + _filename)) {
            fos.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
