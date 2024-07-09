package util;

import java.io.*;

public class Utils {

    public static byte[] getFile(String fileName) throws IOException {
        File file = new File("src/main/resources/static/"+fileName);
        FileInputStream fis = new FileInputStream(file);
        return fis.readAllBytes();
    }
}
