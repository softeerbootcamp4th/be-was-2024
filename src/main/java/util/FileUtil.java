package util;

import java.io.*;

public class FileUtil {

    // to prevent instantiation
    private FileUtil() {
    }

    private static final String STATIC_PATH = "src/main/resources/static";

    public static byte[] readBytesFromFile(String path) throws IOException {
        File file = new File(STATIC_PATH + path);
        int lengthOfBodyContent = (int) file.length();
        byte[] body = new byte[lengthOfBodyContent];
        try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {
            bis.read(body);
        }
        return body;
    }
}
