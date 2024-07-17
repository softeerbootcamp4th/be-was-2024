package http;

import java.util.HashMap;

public class HttpRequest {

    StartLine startLine;
    HashMap<String, String> headers = new HashMap<>();
    byte[] body;

    public HttpRequest setStartLine(StartLine startLine) {
        this.startLine = startLine;
        return this;
    }

    public HttpRequest setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequest setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpMethod getHttpMethod() {
        return startLine.getHttpMethod();
    }

    public String getRequestUrl() {
        return startLine.getRequestUrl();
    }

    public String getHeaders(String key) {
        return headers.get(key);
    }

    public byte[] getBody() {
        return body;
    }

}
