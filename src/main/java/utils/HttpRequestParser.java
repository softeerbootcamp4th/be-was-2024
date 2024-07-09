package utils;

import enums.Method;

import java.io.*;
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
    private final byte[] body;
    private Method method;
    private final Map<String, String> requestHeadersMap = new HashMap<>();
    private final Map<String, String> queryParametersMap = new HashMap<>();
    public HttpRequestParser(InputStream inputStream) throws IOException {
        ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();
        int c;
        int prev1 = -1, prev2 = -1, prev3 = -1;

        // Read headers
        while ((c = inputStream.read()) != -1) {
            headerBuffer.write(c);
            if (prev3 == '\r' && prev2 == '\n' && prev1 == '\r' && c == '\n') {
                break;
            }
            prev3 = prev2;
            prev2 = prev1;
            prev1 = c;
        }

        String headersString = headerBuffer.toString(StandardCharsets.ISO_8859_1);
        String[] headerLines = headersString.split("\r\n");

        startLine = headerLines[0];

        for (int i = 1; i < headerLines.length; i++) {
            String line = headerLines[i];
            int colonIndex = line.indexOf(":");
            if (colonIndex != -1) {
                String key = line.substring(0, colonIndex).trim();
                String value = line.substring(colonIndex + 1).trim();
                requestHeadersMap.put(key, value);
            }
        }

        String contentLengthHeader = requestHeadersMap.get("Content-Length");
        int contentLength = contentLengthHeader == null ? 0 : Integer.parseInt(contentLengthHeader);

        // body 파싱
        if (contentLength > 0) {
            body = new byte[contentLength];
            int bytesRead = inputStream.read(body, 0, contentLength);
            if (bytesRead != contentLength) {
                throw new IOException("Failed to read full request body");
            }
        } else {
            body = null;
        }

        method = Method.fromString(startLine.split(" ")[0]);
        url = startLine.split(" ")[1];
        String[] tokens = url.split("\\?");

        path = tokens[0];
        if (tokens.length > 1) {
            String queryParameters = tokens[1];
            for (String keyValue : queryParameters.split("&")) {
                int equalsIndex = keyValue.indexOf("=");
                if (equalsIndex != -1) {
                    String key = URLDecoder.decode(keyValue.substring(0, equalsIndex), StandardCharsets.UTF_8);
                    String value = URLDecoder.decode(keyValue.substring(equalsIndex + 1), StandardCharsets.UTF_8);
                    queryParametersMap.put(key, value);
                }
            }
        }
    }

    public String getStartLine() {
        return startLine;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public byte[] getBody() {
        return body;
    }

    public Map<String, String> getRequestHeadersMap() {
        return requestHeadersMap;
    }

    public Map<String, String> getQueryParametersMap() {
        return queryParametersMap;
    }

    public Method getMethod() {
        return method;
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
