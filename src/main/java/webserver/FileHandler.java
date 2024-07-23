package webserver;

import webserver.enumPackage.ContentType;

import java.io.*;

/**
 * 파일 정보를 처리해주는 클래스
 */
public class FileHandler {

    /**
     * 읽어온 파일 정보를 바이트 어레이로 변환해주는 메서드
     * @param file 읽어온 파일 정보
     * @return 파일 정보를 바이트 어레이로 변환한 것
     * @throws IOException
     */
    public static byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) { //BufferedInputStream
            byte[] buffer = new byte[baos.size()+1];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    /**
     * 파일의 Content-Type를 지정해주는 메서드
     * @param fileName 읽어온 파일의 이름
     * @return String형태의 File Content-Type
     */
    public static String determineContentType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return ContentType.OCTET_STREAM.getMimeType();
        }
        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        return ContentType.fromExtension(extension);
    }


}
