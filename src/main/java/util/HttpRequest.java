package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP request.
 */
public class HttpRequest {
    private String method;
    private String url;
    private String path;
    private String contentType;
    private String httpVersion;
    private Map<String, String> headers;
    private Map<String, String> queryParams;

    /**
     * Creates a new HttpRequest.
     *
     * @param method the HTTP method
     * @param url the URL
     * @param path the path
     * @param httpVersion the HTTP version
     * @param headers the headers
     * @param queryParams the query parameters
     */
    public HttpRequest(String method, String url, String path, String httpVersion,
                       Map<String, String> headers, Map<String, String> queryParams) {
        this.method = method;
        this.url = url;
        this.path = path;
        this.httpVersion = httpVersion;
        this.headers = new HashMap<>(headers);
        this.queryParams = new HashMap<>(queryParams);
        this.contentType = determineContentType(url);
    }


    public HttpRequest withUrl(String newUrl) {
        return new HttpRequest(this.method, newUrl, this.path, this.httpVersion, this.headers, this.queryParams);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setUrl(String url) {
        this.url = url;
        this.contentType = determineContentType(url);
    }

    public String getContentType() {
        return contentType;
    }

    private String determineContentType(String url) {
        if (url.endsWith(".html") || url.equals("/")) {
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