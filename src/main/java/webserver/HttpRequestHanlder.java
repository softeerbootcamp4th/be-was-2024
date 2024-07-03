package webserver;

import java.util.HashMap;
import java.util.Map;


public class HttpRequestHanlder {
    private String method;
    private String uri;
    private String body;
    private String protocol;
    private Map<String, String> headers = new HashMap<>();

    public HttpRequestHanlder(String startline) {
        String[] split = startline.split(" ");
        method =  split[0];
        uri = split[1];
        protocol = split[2];
    }

    public void addHeader(String key, String value){
        headers.put(key, value);
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}