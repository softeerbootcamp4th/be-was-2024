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
    byte[] body;

    public HttpRequestMessage(String method, String uri, String version, Map<String,String> queryParam, Map<String, String> headers, byte[] body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.queryParam = queryParam;
        this.headers = headers;
        this.body = body;
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

    @Override
    public String toString() {
        return "HttpRequestMessage{" +
                "method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", version='" + version + '\'' +
                ", queryParam=" + queryParam +
                ", headers=" + headers +
                ", body=" +  Arrays.toString(body) +
                '}';
    }
}
