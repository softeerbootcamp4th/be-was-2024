package web;

public class HttpResponse {

    private ResponseCode code;
    private String contentType;

    public HttpResponse() {}

    private HttpResponse(ResponseCode code, String contentType) {
        this.code = code;
        this.contentType = contentType;
    }

    public HttpResponse code(ResponseCode code) {
        this.code = code;
        return this;
    }

    public HttpResponse contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HttpResponse build() {
        return new HttpResponse(code, contentType);
    }

    public ResponseCode getCode() {
        return code;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] get
}
