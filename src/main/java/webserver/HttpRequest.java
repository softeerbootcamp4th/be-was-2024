package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String url;
    private Map<String, String> headers;
    private byte[] body;

    public HttpRequest(String method, String url, Map<String, String> headers, byte[] body) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
