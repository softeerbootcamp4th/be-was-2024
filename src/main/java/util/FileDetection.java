package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileDetection {

    public static String fixedPath;
    static {
        Properties properties = new Properties();
        try (InputStream input = FileDetection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                properties.load(input);
                fixedPath = properties.getProperty("fixedPath");
            } else {
                throw new IOException("config.properties 파일을 찾을 수 없습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getPath(String path) {
        File file = new File(path);
        return file.isDirectory()? path+"/index.html" : path;
    }
}
