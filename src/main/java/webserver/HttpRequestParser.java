package webserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequestParser {
    private String method;
    private String url;
    private ConcurrentHashMap<String, String> headers;
    private byte[] body;

    public HttpRequestParser() {
        headers = new ConcurrentHashMap<>();
    }

    public HttpRequest parse(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int length;

        // Read request line
        String line = readLine(inputStream);
        if (line == null || line.isEmpty()) {
            return null;
        }
        parseRequestLine(line);

        // Parse headers
        while ((line = readLine(inputStream)) != null && !line.isEmpty()) {
            parseHeaderLine(line);
        }

        // Parse body if Content-Length header is present
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            int bytesRead;
            while (contentLength > 0 && (bytesRead = inputStream.read(data, 0, Math.min(data.length, contentLength))) != -1) {
                buffer.write(data, 0, bytesRead);
                contentLength -= bytesRead;
            }
        }

        body = buffer.toByteArray();
        return new HttpRequest(method, url, headers, body);
    }

    private String readLine(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = inputStream.read()) != -1) {
            if (c == '\r') {
                continue;
            }
            if (c == '\n') {
                break;
            }
            sb.append((char) c);
        }
        return sb.toString();
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
}
