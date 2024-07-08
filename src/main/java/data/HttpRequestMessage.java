package data;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestMessage {
    String method;
    String uri;
    String version;
    Map<String, String> queryParam;
    Map<String,String> headers;
    String body;

    public HttpRequestMessage(String method, String uri, String version, Map<String,String> queryParam, Map<String, String> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.queryParam = queryParam;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public Map<String,String> getQueryParam() {
        return queryParam;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
