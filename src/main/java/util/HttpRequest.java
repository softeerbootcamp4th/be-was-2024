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
    private String path;
    private String httpVersion;
    private String content;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> queryParams = new HashMap<>();

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
            this.content = setContentType(this.url);
            parseUrl(this.url);


            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] header = line.split(": ");
                if (header.length == 2) {
                    headers.put(header[0], header[1]);
                }
            }
        }
    }
    private void parseUrl(String url) {
        int queryIndex = url.indexOf("?");
        if (queryIndex != -1) {
            this.path = url.substring(0, queryIndex);
            String queryString = url.substring(queryIndex + 1);
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        } else {
            this.path = url;
        }
    }
    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        this.content = setContentType(url);
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public String getContentType() {
        return content;
    }
    public String getPath() {return path;}
    public Map<String, String> getQueryParams() {return queryParams;}
    private String setContentType(String url) {
        if (url.endsWith(".html")) {
            return "text/html";
        } else if (url.endsWith(".css")) {
            return "text/css";
        } else if (url.endsWith(".js")) {
            return "application/javascript";
        } else if (url.endsWith(".ico")) {
            return "image/x-icon";
        } else if (url.endsWith(".png")) {
            return "image/png";
        } else if (url.endsWith(".jpg") || url.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (url.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            return "application/octet-stream";
        }
    }
}
