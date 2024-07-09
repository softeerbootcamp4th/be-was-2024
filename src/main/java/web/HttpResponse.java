package web;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

    private String httpVersion;
    private ResponseCode statusCode;
    private String contentType;
    private int contentLength;
    private String location;
    private HttpSession session;
    private byte[] body;

    public HttpResponse() {}

    private HttpResponse(
            String httpVersion,
            ResponseCode statusCode,
            String contentType,
            int contentLength,
            String location,
            HttpSession session,
            byte[] body
    ) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.location = location;
        this.session = session;
        this.body = body;
    }

    public static class HttpResponseBuilder {
        private String httpVersion = "HTTP/1.1";
        private ResponseCode code = ResponseCode.OK;
        private String contentType = "*/*";
        private int contentLength = 0;
        private String location = "/";
        private HttpSession session;
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

        public HttpResponseBuilder session(HttpSession session) {
            this.session = session;
            return this;
        }

        public HttpResponseBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(
                    httpVersion, code, contentType, contentLength, location, session, body
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
            response.append("Location: ").append(location).append("\r\n");
        }

        if(contentLength > 0) {
            response.append("Content-Length: ").append(contentLength).append("\r\n");
        }

        if(!contentType.isEmpty()) {
            response.append("Content-Type: ").append(contentType).append("\r\n");
        }
        if(session!=null) {
            response.append("Set-cookie: ").append(session.getString()).append("\r\n");
        }

        response.append("\r\n");

        out.write(response.toString().getBytes());
        if(body!=null) out.write(body);
    }
}
