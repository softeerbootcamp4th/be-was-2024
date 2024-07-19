package util;

import exception.NotExistException;

import java.io.*;

public class Utils {

    public static byte[] getFile(String fileName) throws IOException {
        File file = new File("src/main/resources/static/"+fileName);
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
        }catch (FileNotFoundException e){
            throw new NotExistException();
        }
        return fis.readAllBytes();
    }
}
