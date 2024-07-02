package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestParser {
    private final String startLine;
    private final Map<String, String> headers;
    public HttpRequestParser(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        startLine = br.readLine();
        headers = new HashMap<>();

        String readLine;
        while (!(readLine = br.readLine()).isEmpty()) {
            String[] keyValue = readLine.split(": ");
            headers.put(keyValue[0], keyValue[1]);
        }
    }

    public String getUrl() {
        return startLine.split(" ")[1];
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getExtension() {
        String url = getUrl();
        String regex = "\\.([a-z]+)$";

        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(url);

        // 파일 확장자를 가진 url인지 검증
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
