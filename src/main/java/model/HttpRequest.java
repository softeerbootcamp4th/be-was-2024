package model;

public record HttpRequest(HttpMethod method, String url) {
    public HttpMethod getMethod(){
        return this.method;
    }

    public String getURL(){
        return this.url;
    }
}
