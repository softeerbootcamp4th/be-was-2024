package webserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HttpRequest의 정보를 파싱해주는 메서드
 */
public class HttpRequestParser {
    private String method;
    private String url;
    private ConcurrentHashMap<String, String> headers;
    private byte[] body;

    public HttpRequestParser() {
        headers = new ConcurrentHashMap<>();
    }

    /**
     * 들어온 정보를 형식에 맞춰 파싱해주는 메서드
     * @param inputStream 클라이언트에게 입력되어온 정보
     * @return 파싱된 정보를 형식에 맞춘 HttpRequest
     * @throws IOException
     */
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
        if (headers.containsKey("content-length")) {
            int contentLength = Integer.parseInt(headers.get("content-length"));
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
            String headerName = headerLine.substring(0, colonIndex).trim().toLowerCase(); // Convert to lowercase
            String headerValue = headerLine.substring(colonIndex + 1).trim();
            headers.put(headerName, headerValue);
        }
    }
}
