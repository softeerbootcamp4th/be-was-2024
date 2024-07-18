package webserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 들어온 이미지에 대한 저장을 담당하는 클래스
 */
public class ImageSaver {

    /**
     * 이미지 파일 저장을 담당하는 메서드
     * @param imageBytes 저장하고자 하는 이미지 배열
     * @param directory 이미지가 저장되는 경로
     * @return 저장된 이미지의 랜덤으로 생성된 파일명
     */
    public static String saveImage(byte[] imageBytes, String directory) {
        String fileName = UUID.randomUUID().toString();
        String filePath = directory + "/" + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }
}