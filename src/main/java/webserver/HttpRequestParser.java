package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);
    private static final HttpRequestParser instance = new HttpRequestParser();

    public static HttpRequestParser getInstance() {
        return instance;
    }

    public Map<String, String> parseRequestHeaders(BufferedReader br) throws IOException {
        String line;
        StringBuilder log = new StringBuilder();
        Map<String, String> headers = new HashMap<>();

        // 요청 메시지를 한 줄씩 읽어 StringBuilder 에 추가
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            log.append(line).append("\n");
            int index = line.indexOf(':');
            if (index > 0) {
                String headerName = line.substring(0, index).trim();
                String headerValue = line.substring(index + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

        logger.debug("Http Request Header : {} ", log);

        return headers;
    }

    public String parseRequestURI(String requestLine) {
        // Split the request line by spaces
        String[] tokens = requestLine.split("\\s+");

        // The request URI (path) is the second element in the tokens array
        if (tokens.length >= 2) {
            return tokens[1];
        } else {
            throw new IllegalArgumentException("Invalid HTTP request line: " + requestLine);
        }
    }

    public String parseRequestContentType(String requestLine) {
        String[] pathParts = requestLine.split("/");
        if (pathParts.length < 1) {
            return ContentType.html.getContentType();
        }
        String lastPathPart = pathParts[pathParts.length - 1];

        // 파일 이름에서 확장자 추출
        int lastDotIndex = lastPathPart.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return ContentType.valueOf(lastPathPart.substring(lastDotIndex + 1)).getContentType();
        } else {
            return ContentType.html.getContentType();
        }
    }
}
