package webserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ImageSaver {

    // 이미지 바이트 배열을 파일로 저장하는 메서드
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