package utils;

import config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.*;

public class FileReadUtil {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    /**
     * 클래스 경로에서 데이터를 읽어오는 메서드. 길이 0의 파일을 읽는 것과 파일을 읽지 않는 것은 다르다.
      */
    public static byte[] read(String filePath) throws IOException {
        // classloader로 경로를 얻는 경우 처음에 슬래시가 오면 안된다.
        if(filePath.startsWith("/")) filePath = filePath.substring(1);

        // classloader로부터 경로를 받아 읽어야 build.gradle에 정의한 경로를 얻을 수 있음.
        // FileInputStream으로 바로 얻으면 static 경로 모두 정의해야 함.
        try(InputStream is = FileReadUtil.class.getClassLoader().getResourceAsStream(filePath)) {
            if(is == null) throw new FileNotFoundException("파일을 찾을 수 없음");

            return is.readAllBytes();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 파일 경로에서 데이터를 읽어오는 메서드
     */
    public static byte[] readFromLocal(String filePath) throws IOException {
        String _filename = filePath.startsWith("/") ? filePath.substring(1) : filePath;
        try(FileInputStream fis = new FileInputStream(AppConfig.FILE_SRC +  _filename)) {
            return fis.readAllBytes();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}