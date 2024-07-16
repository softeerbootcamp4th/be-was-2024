package util;

import java.io.*;

import static util.constant.StringConstants.RESOURCE_PATH;


public class FileMapper {

    public static byte[] getByteConvertedFile(String path) throws IOException {
        File file = new File(RESOURCE_PATH + path);
        InputStream fileInputStream = new FileInputStream(file);
        return fileInputStream.readAllBytes();
    }
}
