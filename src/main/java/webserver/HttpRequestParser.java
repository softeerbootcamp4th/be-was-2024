package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
    private String method;
    private String url;
    private Map<String, String> headers;
    private byte[] body;

    public HttpRequestParser() {
        headers = new HashMap<>();
    }

    public HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        // Read request line
        String line = br.readLine();
        if (line == null || line.isEmpty()) {
            return null;
        }
        parseRequestLine(line);

        // Parse headers
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            parseHeaderLine(line);
        }

        // Parse body if Content-Length header is present
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            parseBody(br, contentLength);
        }

        return new HttpRequest(method, url, headers, body);
    }

    private void parseRequestLine(String requestLine) {
        String[] parts = requestLine.trim().split("\\s+");
        method = parts[0];
        url = parts[1];
    }

    private void parseHeaderLine(String headerLine) {
        int colonIndex = headerLine.indexOf(':');
        if (colonIndex != -1) {
            String headerName = headerLine.substring(0, colonIndex).trim();
            String headerValue = headerLine.substring(colonIndex + 1).trim();
            headers.put(headerName, headerValue);
        }
    }

    private void parseBody(BufferedReader br, int contentLength) throws IOException {
        char[] buffer = new char[contentLength];
        int bytesRead = 0;
        int totalRead = 0;

        while (totalRead < contentLength && (bytesRead = br.read(buffer, totalRead, contentLength - totalRead)) != -1) {
            totalRead += bytesRead;
        }

        body = new String(buffer, 0, totalRead).getBytes("UTF-8");
    }
}
