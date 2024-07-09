package web;

import java.util.LinkedList;

public class HttpRequest {

    private HttpMethod method;
    private String path;
    private LinkedList<String> accept;
    private int contentLength;
    private String contentType;
    private HttpSession session;
    private byte[] body;

    public HttpRequest() {}

    private HttpRequest(
            HttpMethod method,
            String path,
            LinkedList<String> accept,
            int contentLength,
            String contentType,
            byte[] body
    ) {
        this.method = method;
        this.path = path;
        this.accept = accept;
        this.contentLength = contentLength;
        this.contentType = contentType;
        this.session = session;
        this.body = body;
    }

    public static class HttpRequestBuilder {
        private HttpMethod method;
        private String path;
        private LinkedList<String> accept;
        private int contentLength;
        private String contentType;
        private HttpSession session;
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

        public HttpRequestBuilder session(HttpSession session) {
            this.session = session;
            return this;
        }

        public HttpRequestBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(
                    method, path, accept, contentLength, contentType, body
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
}
