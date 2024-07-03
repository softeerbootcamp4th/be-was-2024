package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    private String method;
    private String url;
    private String httpVersion;
    private Map<String, String> headers = new HashMap<>();

    public HttpRequest(BufferedReader reader) {
        try {
            parseRequest(reader);
        } catch (IOException e) {
            logger.error("Error parsing HTTP request: " + e.getMessage());
        }
    }

    private void parseRequest(BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null && !line.isEmpty()) {
            String[] requestLine = line.split(" ");
            this.method = requestLine[0];
            this.url = requestLine[1];
            this.httpVersion = requestLine[2];
            logger.debug("Requested URL: " + url);

            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] header = line.split(": ");
                if (header.length == 2) {
                    headers.put(header[0], header[1]);
                }
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }
}
