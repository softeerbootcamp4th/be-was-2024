package utils;

import java.nio.file.Path;

public class PathParser {

    private static class LazyManager {
        public static PathParser instance = new PathParser();
    }

    private PathParser() {}

    public static PathParser getInstance() {
       return LazyManager.instance;
    }

    public String fileExtExtract(String fileName) {
        int pointPos = fileName.lastIndexOf(".");
        if(pointPos == -1) {
           throw new IllegalArgumentException("파일 확장자가 존재하지 않는 파일 이름입니다.");
        }
        return fileName.substring(pointPos);
    }

}
