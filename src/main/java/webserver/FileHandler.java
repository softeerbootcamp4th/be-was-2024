package webserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
    public static byte[] getFileContent(String path) throws IOException {
        String str = "";
        BufferedReader br = new BufferedReader(new FileReader(path));
        String fileLine = br.readLine();

        while (fileLine != null) {
            str += fileLine;
            fileLine = br.readLine();
        }
        return str.getBytes();
    }
}
