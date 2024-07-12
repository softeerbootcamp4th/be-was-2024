package webserver;

import java.io.File;
import java.util.HashMap;

public class Request {
    private final String method;
    private final String path;
    private final String queryString;

    protected Request(String requestLine) {
        this.method = requestLine.split(" ")[0];
        String url = requestLine.split(" ")[1];
        if (url.contains("?")) {
            this.path = url.split("\\?")[0];
            this.queryString = url.split("\\?")[1];
        } else {
            this.path = url;
            this.queryString = "";
        }
    }

    public static Request from(String line) {
        return new Request(line);
    }

    public boolean isQueryString() {
        // 일단은 "?"를 포함하는지만 검사.
        // 이후 예외처리 고민해볼 필요 있음
        return !queryString.isEmpty();
    }

    private String getStaticPath() {
        File testFile = new File("src/main/resources/static" + path);

        if (testFile.isDirectory()) {
            return path + "/index.html";
        } else {
            return path;
        }
    }

    public String getHttpMethod() {
        return method;
    }

    public String getPath() {
        return getStaticPath();
    }

    public String getQueryString() {
        return this.queryString;
    }

    public HashMap<String, String> parseQueryString() {
        String[] splitedQeuryString = queryString.split("&");
        HashMap<String, String> userData = new HashMap<>();
        for (String s : splitedQeuryString) {
            String[] fieldData = s.split("=");
            userData.put(fieldData[0], fieldData[1]);
        }
        return userData;
    }
}
