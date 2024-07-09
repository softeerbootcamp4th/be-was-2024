package webserver.front.data;

import webserver.back.byteReader.Body;

public class HttpRequest extends HttpMessage{
    private String firstLine;
    private String method;
    private String url;

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
    public HttpRequest(String httpVersion, String method, String url) {
        super(httpVersion);
        this.method = method;
        this.url = url;
    }
    public HttpRequest( String httpVersion, String method, String url,byte[] body,String contentType) {
        super(httpVersion,body,contentType);
        this.method = method;
        this.url = url;
    }
}
