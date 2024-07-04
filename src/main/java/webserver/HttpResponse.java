package webserver;

public class HttpResponse {
    public String httpVersion;
    public String contentType;
    public String statusCode;
    public String statusText;
    public int contentLength;

    public byte[] body;

    public HttpResponse(String httpVersion, String statusCode, String statusText, byte[] body,String contentType) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.body = body;
        setContentInform(body, contentType);
    }

    public void setContentInform(byte[] body, String contentType){
        this.contentLength = body.length;
        this.contentType = contentType;
    }
    public String getHttpVersion() {
        return httpVersion;
    }

    public String getContentType() {
        return contentType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public int getContentLength() {
        return contentLength;
    }
}

