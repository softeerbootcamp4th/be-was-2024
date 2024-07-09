package web;

import java.util.LinkedList;

public class HttpRequest {

    private HttpMethod method;
    private String path;
    private LinkedList<String> accept;

    public HttpRequest() {}

    private HttpRequest(HttpMethod method, String path, LinkedList<String> accept) {
        this.method = method;
        this.path = path;
        this.accept = accept;
    }

    public HttpRequest method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public HttpRequest path(String path) {
        this.path = path;
        return this;
    }

    public HttpRequest accept(LinkedList<String> accept) {
        this.accept = accept;
        return this;
    }

    public HttpRequest build() {
        return new HttpRequest(method, path, accept);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public LinkedList<String> getAccept() {
        return accept;
    }
}
