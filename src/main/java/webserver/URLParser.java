package webserver;

import java.io.File;

public class URLParser {
    public String getURL(String line) {
        String url = line.split(" ")[1];

        if (!url.contains("?")) { // 쿼리 스트링이 아닌 경우
            return getStaticUrl(url);
        } else { // 쿼리스트링인 경우
            System.out.println("asdfasdfasdf");
            return "/index.html";
        }
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
