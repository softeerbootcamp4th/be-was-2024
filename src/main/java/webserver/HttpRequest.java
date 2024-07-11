package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpRequest {
    private String method;
    private String url;
    private ConcurrentHashMap<String, String> headers;
    private byte[] body;

    public HttpRequest(String method, String url, ConcurrentHashMap<String, String> headers, byte[] body) {
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
