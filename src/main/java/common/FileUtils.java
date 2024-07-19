package common;

import file.ViewFile;
import web.HttpRequest;

public class FileUtils {

    public static final String STATIC_DIR_PATH = "./src/main/resources/static";

    public static String getStaticFilePath(String path) {
        return STATIC_DIR_PATH+path;
    }

    public static String getExtensionFromPath(String path) {
        return path.split("\\.")[1];
    }

    public static ViewFile makeFileFromRequest(HttpRequest request) {
        String filePath = request.getPath();
        String extension = FileUtils.getExtensionFromPath(filePath);

        return new ViewFile(filePath, extension);
    }
}
