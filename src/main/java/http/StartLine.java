package http;

public class StartLine {
    private HttpMethod httpMethod;
    private String requestUrl;
    private String version;

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getVersion() {
        return version;
    }

    public StartLine(HttpMethod httpMethod, String requestUrl, String version) {
        this.httpMethod = httpMethod;
        this.requestUrl = requestUrl;
        this.version = version;
    }
}
