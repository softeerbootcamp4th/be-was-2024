package common;

import file.ViewFile;
import web.HttpRequest;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 파일 저장, 확장자 추출, 파일 생성 등을 담당하는 유틸리티 클래스
 */
public class FileUtils {

    public static final String STATIC_DIR_PATH = "./src/main/resources/static";
    public static final String MY_ARTICLE_PATH = "/eckrin/";

    public static String getStaticFilePath(String path) {
        return STATIC_DIR_PATH+path;
    }

    public static String getExtensionFromPath(String path) {
        return path.split("\\.")[1];
    }

    public static String getImageExtensionFromPath(String body) {
        String[] bodySplit = body.split("image/");
        if(bodySplit.length>=2) return bodySplit[1];
        else return null;
    }

    public static ViewFile makeFileFromRequest(HttpRequest request) {
        String filePath = request.getPath();
        String extension = FileUtils.getExtensionFromPath(filePath);

        return new ViewFile(filePath, extension);
    }

    public static String saveFile(byte[] data, String extension) {
        String fileName = UUID.randomUUID()+"."+extension;
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(STATIC_DIR_PATH+ MY_ARTICLE_PATH+fileName))) {
            bos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
