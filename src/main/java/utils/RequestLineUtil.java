package utils;

import java.io.File;

public class RequestLineUtil {
    public String getURL(String line) {
        String url = line.split(" ")[1];
        return getStaticPath(url);
    }

    public boolean isQueryString(String line) {
        // 일단은 "?"를 포함하는지만 검사.
        // 이후 예외처리 고민해볼 필요 있음
        return line.contains("?");
    }

    public static String getStaticPath(String path) {
        File testFile = new File("src/main/resources/static" + path);

        if (testFile.exists()) {
            if (testFile.isDirectory()) {
                return path + "/index.html";
            } else {
                return path;
            }
        } else {
            return path;
        }
    }

    public String getHttpMethod(String line) {
        return line.split(" ")[0];
    }
}
