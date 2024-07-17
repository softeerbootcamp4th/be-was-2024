package webserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ImageSaver {

    // 이미지 바이트 배열을 파일로 저장하는 메서드
    public static void saveImage(byte[] imageBytes, String directory, String fileName) {
        String filePath = directory + "/" + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(imageBytes);
            System.out.println("이미지 파일이 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("이미지 파일 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}