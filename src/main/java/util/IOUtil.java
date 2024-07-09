package util;

import java.io.*;
import java.util.Properties;

public class IOUtil {

    // to prevent instantiation
    private IOUtil() {
    }

    public static boolean isDirectory(boolean isStatic, String path) {
        try(InputStream input = IOUtil.class.getClassLoader().getResourceAsStream(StringUtil.PROPERTIES)){
            if(input == null){
                throw new FileNotFoundException();
            }
            Properties prop = new Properties();
            prop.load(input);

            String fullPath = (isStatic ? prop.getProperty(StringUtil.STATIC_DIR) : prop.getProperty(StringUtil.TEMPLATES_DIR)) + path;
            File file = new File(fullPath);
            return file.isDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static byte[] readBytesFromFile(boolean isStatic, String path) throws IOException {
        try (InputStream input = IOUtil.class.getClassLoader().getResourceAsStream(StringUtil.PROPERTIES)){
            if(input == null){
                throw new FileNotFoundException();
            }
            Properties prop = new Properties();
            prop.load(input);

            String fullPath = (isStatic ? prop.getProperty(StringUtil.STATIC_DIR) : prop.getProperty(StringUtil.TEMPLATES_DIR)) + path;
            File file = new File(fullPath);
            if(file.isDirectory()){
                file = new File(fullPath + StringUtil.INDEX_HTML);
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

    public static byte[] convertObjectToBytes(Object obj) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        try (ObjectOutputStream ois = new ObjectOutputStream(boas)) {
            ois.writeObject(obj);
            return boas.toByteArray();
        }
    }
}
