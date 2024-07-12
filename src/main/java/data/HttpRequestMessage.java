package data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestMessage {
    String method;
    String uri;
    String version;
    Map<String, String> queryParam;
    Map<String,String> headers;
    Map<String,String> cookies;
    byte[] body;

    public HttpRequestMessage(String method, String uri, String version, Map<String,String> queryParam, Map<String, String> headers, byte[] body, Map<String,String> cookies) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.queryParam = queryParam;
        this.headers = headers;
        this.body = body;
        this.cookies = cookies;
    }

    public void setBody(byte[] body) {
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

    public byte[] getBody() {
        return body;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    @Override
    public String toString() {
        return "HttpRequestMessage{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", version='" + version + '\'' +
                ", queryParam=" + queryParam +
                ", headers=" + headers +
                ", cookies=" + cookies +
                ", body=" + Arrays.toString(body) +
                '}';
    }
}
