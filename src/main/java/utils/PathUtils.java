package utils;

import enums.MimeType;

public class PathUtils {

    private PathUtils() {}

    public static String fileExtExtract(String fileName) {
        int pointPos = fileName.lastIndexOf(".");
        if(pointPos == -1) {
           throw new IllegalArgumentException("파일 확장자가 존재하지 않는 파일 이름입니다.");
        }
        return fileName.substring(pointPos);
    }

    public static String filePathResolver(String fileName) {
        if(fileName.contains(".")) {
            return fileName;
        }
        return fileName + ".html";
    }

    public static MimeType extToMimetype(String filePath) {
        // 파일 확장자를 mimeType으로 바꾸는 과정
        String fileExt = fileExtExtract(filePath);
        MimeTypeMapper mimeTypeMapper = new MimeTypeMapper();
        return mimeTypeMapper.getMimeType(fileExt);
    }
}
