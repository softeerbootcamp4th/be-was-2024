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

    public void parse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line = br.readLine();

        if (line == null || line.isEmpty()) {
            return;
        }

        String[] requestLine = line.trim().split("\\s+");
        method = requestLine[0]; // e.g., "GET" or "POST"
        url = requestLine[1];

        // Parse headers
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            int colonIndex = line.indexOf(':');
            if (colonIndex != -1) {
                String headerName = line.substring(0, colonIndex).trim();
                String headerValue = line.substring(colonIndex + 1).trim();
                headers.put(headerName, headerValue);
            }
        }

        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] buffer = new char[contentLength];
            int bytesRead = 0;
            int totalRead = 0;

            while (totalRead < contentLength && (bytesRead = br.read(buffer, totalRead, contentLength - totalRead)) != -1) {
                totalRead += bytesRead;
            }

            body = new String(buffer, 0, totalRead).getBytes("UTF-8");
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
