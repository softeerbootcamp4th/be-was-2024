package util;

import java.io.*;

public class Utils {

    final static char[] endCharacters = {'\r', '\n', '\r', '\n'};

    public static byte[] getFile(String fileName) throws IOException {
        File file = new File("src/main/resources/static/"+fileName);
        FileInputStream fis = new FileInputStream(file);
        return fis.readAllBytes();
    }

    public static String getAllStrings(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        int endCharactersCount=0;

        while(endCharactersCount!=endCharacters.length){

            int read = br.read();
            if(endCharacters[endCharactersCount]==read) endCharactersCount++;
            else endCharactersCount = 0;
            sb.append((char)read);

        }

        return sb.toString();
    }

}
