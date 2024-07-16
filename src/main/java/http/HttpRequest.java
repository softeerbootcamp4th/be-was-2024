package http;

import java.util.HashMap;

public class HttpRequest {

    HttpMethod httpMethod;
    String requestUrl;
    String version;
    HashMap<String, String> headers = new HashMap<>();
    byte[] body;


    public void setStartLine(HttpMethod httpMethod, String requestUrl, String version) {
        this.httpMethod = httpMethod;
        this.requestUrl = requestUrl;
        this.version = version;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public HttpMethod getHttpMethod() {
        return this.httpMethod;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public String getHeaders(String key) {
        return headers.get(key);
    }

    public byte[] getBody() {
        return body;
    }

}
