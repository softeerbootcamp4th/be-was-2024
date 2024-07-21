package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * 파일을 찾기 위한 기본 경로를 설정하고 디렉토리인지 아닌지 판단하는 클래스
 */
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


    /**
     * 해당 path가 디렉토리인지 아닌지 판단후 디렉토리이면 /index.html을 추가해준다
     * @param path 판단해야 하는 경로
     */
    public static String getPath(String path) {
        File file = new File(path);
        return file.isDirectory()? path+"/index.html" : path;
    }
}
