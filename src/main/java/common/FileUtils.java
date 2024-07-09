package common;

public class FileUtils {

    public static String getExtensionFromPath(String path) {
        return path.split("\\.")[1];
    }
}
