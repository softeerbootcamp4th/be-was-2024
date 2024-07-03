package webserver;

import java.io.File;

public class URLParser {
    public String getURL(String line) {
        String url = line.split(" ")[1];
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
