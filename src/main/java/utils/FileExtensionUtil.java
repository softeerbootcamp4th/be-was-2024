package utils;

public class FileExtensionUtil {
    public static String getFileExtension(String fileName) {
        String[] extensions = fileName.split("\\.");
        // 확장자가 없으면 null 반환
        if(extensions.length == 1) return null;
        return extensions[extensions.length - 1];
    }
}
