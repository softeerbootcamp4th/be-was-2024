package utils;

import java.io.File;

public class RequestLineUtil {
    public String getURL(String line) {
        String url = line.split(" ")[1];
        return getStaticPath(url);
    }

    public static boolean isQueryString(String url) {
        // 일단은 "?"를 포함하는지만 검사.
        // 이후 예외처리 고민해볼 필요 있음
        return url.contains("?");
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

    public static String getHttpMethod(String requestLine) {
        return requestLine.split(" ")[0];
    }

    public static String getUrl(String requestLine) {
        return requestLine.split(" ")[1];
    }

    public static String getPath(String url) {
        if (url.contains("?")) {
            return url.split("\\?")[0];
        } else {
            return url;
        }
    }

    public static String getQueryString(String url) {
        if (url.contains("?")) {
            return url.split("\\?")[1];
        } else {
            return "";
        }
    }
}
