package webserver;

import java.io.File;

public class URLParser {
    public String getURL(String line) {
        String url = line.split(" ")[1];
        return getStaticUrl(url);
    }

    public boolean isQueryString(String line) {
        // 일단은 "?"를 포함하는지만 검사.
        // 이후 예외처리 고민해볼 필요 있음
        return line.contains("?");
    }

    private String getStaticUrl(String url) {
        File testFile = new File("src/main/resources/static" + url);

        if (testFile.isDirectory()) {
            return url + "/index.html";
        } else {
            return url;
        }
    }

    public String getHttpMethod(String line) {
        return line.split(" ")[0];
    }
}
