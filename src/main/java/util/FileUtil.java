package util;

import java.io.*;

public class FileUtil {

    // to prevent instantiation
    private FileUtil() {
    }

    public static final String STATIC_PATH = "src/main/resources/static";

    public static boolean isDirectory(String path) {
        File file = new File(path);
        return file.isDirectory();
    }

    public static byte[] readBytesFromFile(String path) throws IOException {
        try {
            File file = new File(path);
            if(file.isDirectory()){
                file = new File(path + "/index.html");
            }
            int lengthOfBodyContent = (int) file.length();
            byte[] body = new byte[lengthOfBodyContent];
            try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis)) {
                bis.read(body);
            }
            return body;
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }
}
