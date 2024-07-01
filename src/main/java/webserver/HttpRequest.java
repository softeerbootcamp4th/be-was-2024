package webserver;

public class HttpRequest {
    private String method;
    private String url;
    private String mimeTypeForClient;

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getMimeTypeForClient() {
        return mimeTypeForClient;
    }

    public HttpRequest(String method, String url, String mimeTypeForClient) {
        this.method = method;
        this.url = url;
        this.mimeTypeForClient = mimeTypeForClient;
    }
}
