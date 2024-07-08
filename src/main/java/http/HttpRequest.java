package http;

import java.util.HashMap;

public class HttpRequest {
    HttpMethod httpMethod;
    String requestUrl;
    HashMap<String, String> headers = new HashMap<>();

    public HttpRequest(HttpMethod httpMethod, String requestUrl, HashMap<String, String> headers) {
        this.httpMethod = httpMethod;
        this.requestUrl = requestUrl;
        this.headers = headers;
    }

    public HttpMethod getHttpMethod(){
        return this.httpMethod;
    }

    public String getRequestUrl(){
        return this.requestUrl;
    }

    public String getHeaders(String key){
        return this.headers.get(key);
    }
}
