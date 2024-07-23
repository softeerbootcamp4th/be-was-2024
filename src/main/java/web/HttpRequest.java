package web;

import java.util.LinkedList;
import java.util.Map;

/**
 * HTTP 요청에 대한 정보를 담는 POJO
 */
public class HttpRequest {

    private HttpMethod method;
    private String path;
    private LinkedList<String> accept;
    private int contentLength;
    private String contentType;
    private Map<String, String> cookie;
    private byte[] body;

    public HttpRequest() {}

    private HttpRequest(
            HttpMethod method,
            String path,
            LinkedList<String> accept,
            int contentLength,
            String contentType,
            Map<String, String> cookie,
            byte[] body
    ) {
        this.method = method;
        this.path = path;
        this.accept = accept;
        this.contentLength = contentLength;
        this.contentType = contentType;
        this.cookie = cookie;
        this.body = body;
    }

    public static class HttpRequestBuilder {
        private HttpMethod method;
        private String path;
        private LinkedList<String> accept;
        private int contentLength;
        private String contentType;
        private Map<String, String> cookie;
        private byte[] body;

        public HttpRequestBuilder method(HttpMethod method) {
            this.method = method;
            return this;
        }

        public HttpRequestBuilder path(String path) {
            this.path = path;
            return this;
        }

        public HttpRequestBuilder accept(LinkedList<String> accept) {
            this.accept = accept;
            return this;
        }

        public HttpRequestBuilder contentLength(int contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public HttpRequestBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpRequestBuilder cookie(Map<String, String> cookie) {
            this.cookie = cookie;
            return this;
        }

        public HttpRequestBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(
                    method, path, accept, contentLength, contentType, cookie, body
            );
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public LinkedList<String> getAccept() {
        return accept;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public Map<String, String> getCookie() {
        return cookie;
    }

    public byte[] getBody() {
        return body;
    }

    public boolean isGetRequest() {
        try {
            return this.method.equals(HttpMethod.GET);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isPostRequest() {
        try {
            return this.method.equals(HttpMethod.POST);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public String getPathWithoutQueryParam() {
        return this.getPath().split("\\?")[0];
    }
}
