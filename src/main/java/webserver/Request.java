package webserver;

import java.io.File;
import java.util.HashMap;

public class Request {
    private final String method;
    private final String path;

    protected Request(String requestLine) {
        this.method = requestLine.split(" ")[0];
        this.path = requestLine.split(" ")[1];
    }

    public static Request from(String line) {
        return new Request(line);
    }

    public boolean isQueryString() {
        // 일단은 "?"를 포함하는지만 검사.
        // 이후 예외처리 고민해볼 필요 있음
        return path.contains("?");
    }

    public String getStaticPath() {
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
        return path;
    }

    public HashMap<String, String> parseQueryString() {
        String[] splitedQeuryString = splitQueryString(path);
        HashMap<String, String> userData = new HashMap<>();
        for (String s : splitedQeuryString) {
            String[] fieldData = s.split("=");
            userData.put(fieldData[0], fieldData[1]);
        }
        return userData;
    }

    private String[] splitQueryString(String queryString) {
        int questionMarkIndex = queryString.indexOf('?');
        String dataString = queryString.substring(questionMarkIndex + 1);
        return dataString.split("&");
    }
}
