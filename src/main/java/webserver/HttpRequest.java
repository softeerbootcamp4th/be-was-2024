package webserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HttpRequest 형식을 담고 있는 클래스
 */
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
