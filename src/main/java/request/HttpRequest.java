package request;

import http.HttpMethod;

import java.util.HashMap;

public class HttpRequest {
    HttpMethod httpMethod;
    String requestUrl;
    HashMap<String, String> headers = new HashMap<>();

    HttpRequest(HttpMethod httpMethod, String requestUrl, HashMap<String, String> headers) {
        this.httpMethod = httpMethod;
        this.requestUrl = requestUrl;
        this.headers = headers;
    }
}
