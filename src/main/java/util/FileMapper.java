package util;

import java.io.*;

public class FileMapper {
    private static final String RESOURCE_PATH = "src/main/resources/static";

    public static byte[] getByteConvertedFile(String path) throws IOException {
        File file = new File(RESOURCE_PATH + path);
        InputStream fileInputStream = new FileInputStream(file);
        return fileInputStream.readAllBytes();
    }
}
