package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestParser {
    private final String startLine;
    private final String url;
    private final String path;
    private final Map<String, String> requestHeadersMap = new HashMap<>();
    private final Map<String, String> queryParametersMap = new HashMap<>();
    public HttpRequestParser(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        startLine = br.readLine();

        String readLine;
        while ((readLine = br.readLine()) != null && !readLine.isEmpty()) {
            int colonIndex = readLine.indexOf(":");

            if (colonIndex != -1) {
                String key = readLine.substring(0, colonIndex).trim();
                String value = readLine.substring(colonIndex + 1).trim();

                requestHeadersMap.put(key, value);
            }
        }

        url = startLine.split(" ")[1];
        String[] tokens = url.split("\\?");

        path = tokens[0];
        if (tokens.length > 1) {
            String queryParameters = tokens[1];
            for (String queryParameter : queryParameters.split("&")) {
                String[] keyValue = queryParameter.split("=");
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                queryParametersMap.put(key, value);
            }
        }
    }

    public String getStartLine() {
        return this.startLine;
    }

    public String getUrl() {
        return this.url;
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, String> getQueryParametersMap() {
        return this.queryParametersMap;
    }

    public Map<String, String> getRequestHeadersMap() {
        return this.requestHeadersMap;
    }

    public String headersToString() {
        StringBuilder sb = new StringBuilder();
        for (String key : requestHeadersMap.keySet()) {
            sb.append(key).append(": ").append(requestHeadersMap.get(key));
        }

        return sb.toString();
    }

    public String getExtension() {
        String regex = "\\.([a-z]+)$";

        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(path);

        // 파일 확장자를 가진 url인지 검증
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
