package web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpResponse {

    private String httpVersion;
    private ResponseCode statusCode;
    private String contentType;
    private int contentLength;
    private String location;
    private Map<String, String> cookie;
    private byte[] body;

    public HttpResponse() {}

    private HttpResponse(
            String httpVersion,
            ResponseCode statusCode,
            String contentType,
            int contentLength,
            String location,
            Map<String, String> cookie,
            byte[] body
    ) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.location = location;
        this.cookie = cookie;
        this.body = body;
    }

    public static class HttpResponseBuilder {
        private String httpVersion = "HTTP/1.1";
        private ResponseCode code = ResponseCode.OK;
        private String contentType = "*/*";
        private int contentLength = 0;
        private String location = "/";
        private Map<String, String> cookie = new ConcurrentHashMap<>();
        private byte[] body = null;

        public HttpResponseBuilder httpVersion(String httpVersion) {
            this.httpVersion = httpVersion;
            return this;
        }

        public HttpResponseBuilder code(ResponseCode code) {
            this.code = code;
            return this;
        }

        public HttpResponseBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public HttpResponseBuilder contentLength(int contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public HttpResponseBuilder location(String location) {
            this.location = location;
            return this;
        }

        public HttpResponseBuilder cookie(Map<String, String> cookie) {
            this.cookie = cookie;
            return this;
        }

        public HttpResponseBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(
                    httpVersion, code, contentType, contentLength, location, cookie, body
            );
        }

    }

    public ResponseCode getStatusCode() {
        return statusCode;
    }

    public String getContentType() {
        return contentType;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void writeInBytes(OutputStream out) throws IOException {
        StringBuilder response = new StringBuilder();
        response
                .append(httpVersion).append(" ")
                .append(statusCode.getCode()).append(" ")
                .append(statusCode.getMessage()).append("\n");

        if(!location.isEmpty()) {
            response.append(HeaderKey.LOCATION.getKey()).append(": ").append(location).append("\r\n");
        }
        if(contentLength > 0) {
            response.append(HeaderKey.CONTENT_LENGTH.getKey()).append(": ").append(contentLength).append("\r\n");
        }
        if(!contentType.isEmpty()) {
            response.append(HeaderKey.CONTENT_TYPE.getKey()).append(": ").append(contentType).append("\r\n");
        }
        if(!cookie.isEmpty()) {
            response.append(HeaderKey.SET_COOKIE.getKey()).append(": ");
            for(String name : cookie.keySet()) {
                String value = cookie.get(name);
                response.append(name).append("=").append(value).append(";");
            }
            response.deleteCharAt(response.length()-1);
        }
        response.append("\r\n");

        out.write(response.toString().getBytes());
        if(body!=null) out.write(body);
    }
}
