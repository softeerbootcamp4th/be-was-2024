package webserver;

import type.MIMEType;
import type.StatusCodeType;

public class RequestResult {
    private StatusCodeType statusCode;
    private String contentType = MIMEType.DEFAULT.getContentType();
    private byte[] content;

    public RequestResult(StatusCodeType statusCode, byte[] content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    public RequestResult(String contentType, StatusCodeType statusCode, byte[] content) {
        this.contentType = contentType;
        this.statusCode = statusCode;
        this.content = content;
    }

    public RequestResult(StatusCodeType statusCode, String content) {
        this.statusCode = statusCode;
        this.content = content.getBytes();
    }

    public String getContentType() { return contentType; }

    public StatusCodeType getStatusCode() { return statusCode; }

    public byte[] getContent() { return content; }
}
