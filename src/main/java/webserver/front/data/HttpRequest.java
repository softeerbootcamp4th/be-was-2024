package webserver.front.data;

public class HttpRequest {
    private String method;
    private String url;

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
    public HttpRequest(String method, String url) {
        this.method = method;
        this.url = url;
    }
}
