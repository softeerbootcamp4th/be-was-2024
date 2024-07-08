package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private static final Logger logger = LoggerFactory.getLogger(Request.class);

    private final String method;
    private final String path;
    private final String queryString;
    private final String body;
    private Map<String, String> headers = new HashMap<>();

    protected Request(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String requestLine = br.readLine();
        this.method = requestLine.split(" ")[0];
        String url = requestLine.split(" ")[1];
        if (url.contains("?")) {
            this.path = url.split("\\?")[0];
            this.queryString = url.split("\\?")[1];
        } else {
            this.path = url;
            this.queryString = "";
        }

        //읽어들인 InputStream 모두 출력
        String line;
        while (!(line = br.readLine()).isEmpty()) {
            logger.debug(line); // 읽어들인 라인 출력
            int colonIndex = line.indexOf(":");
            String key = line.substring(0, colonIndex);
            String value = line.substring(colonIndex + 1);
            headers.put(key.trim(), value.trim());
        }

        int length = 0;
        if (headers.containsKey("Content-Length")) {
            length = Integer.parseInt(headers.get("Content-Length"));
        }
        char[] buf = new char[length]; // http body 읽어오기
        if (length > 0) {
            br.read(buf);
            System.out.println(buf);
        }
        this.body = new String(buf);
    }

    public static Request from(InputStream inputStream) throws IOException {
        return new Request(inputStream);
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

    public HashMap<String, String> parseBody() {
        String[] splitedQeuryString = body.split("&");
        HashMap<String, String> userData = new HashMap<>();
        for (String s : splitedQeuryString) {
            String[] fieldData = s.split("=");
            userData.put(fieldData[0], fieldData[1]);
        }
        return userData;
    }
}
