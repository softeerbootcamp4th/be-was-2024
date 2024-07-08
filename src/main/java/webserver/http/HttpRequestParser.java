package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestParser.class);
    private static final HttpRequestParser instance = new HttpRequestParser();
    
    public static final String CONTENT_LENGTH = "Content-Length";

    private HttpRequestParser() {
    }

    public static HttpRequestParser getInstance() {
        return instance;
    }

    public MyHttpRequest parseRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        // Read the request line
        String requestLine = br.readLine();
        if (requestLine == null) {
            return null;
        }

        String[] requestLineParts = parseRequestFirstLine(requestLine);
        String method = requestLineParts[0];

        String[] url = requestLineParts[1].split("\\?");

        String path = url[0];
        Map<String, String> queries = new HashMap<>();
        if (url.length > 1) {
            queries = parseQuery(queries, url[1]);
        }

        String version = requestLineParts[2];

        // Read the request headers
        Map<String, String> requestHeaders = new HashMap<>();

        while ((requestLine = br.readLine()) != null && !requestLine.isEmpty()) {
            int index = requestLine.indexOf(':');
            if (index > 0) {
                String headerName = requestLine.substring(0, index).trim();
                String headerValue = requestLine.substring(index + 1).trim();
                requestHeaders.put(headerName, headerValue);
            }
        }

//         Read the request body
        char[] requestBody = null;
        if (requestHeaders.containsKey(CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(requestHeaders.get(CONTENT_LENGTH));
            requestBody = new char[contentLength];
            br.read(requestBody, 0, contentLength);
        }

        return new MyHttpRequest(method, path, queries, version, requestHeaders, requestBody);
    }


    public String[] parseRequestFirstLine(String requestLine) {
        // Split the request line by spaces
        String[] firstLines = requestLine.split("\\s+");

        // The request URI (path) is the second element in the tokens array
        if (firstLines.length >= 2) {
            return firstLines;
        } else {
            throw new IllegalArgumentException("Invalid HTTP request line: " + requestLine);
        }
    }

    public Map<String, String> parseQuery(Map<String, String> queries, String query) {
        String[] keyValues = query.split("&");
        for (String keyValue : keyValues) {
            String[] keyAndValue = keyValue.split("=");
            String key = URLDecoder.decode(keyAndValue[0], StandardCharsets.UTF_8);
            String value = URLDecoder.decode(keyAndValue[1], StandardCharsets.UTF_8);
            queries.put(key, value);
        }

        return queries;
    }
}
