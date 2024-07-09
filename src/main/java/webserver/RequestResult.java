package webserver;

import type.MIMEType;
import type.StatusCodeType;

import java.util.HashMap;

public class RequestResult {
    private StatusCodeType statusCode;
    private HashMap<String, String> responseHeader;
    private byte[] bodyContent;

    public RequestResult(StatusCodeType statusCode, HashMap<String, String> responseHeader, byte[] content) {
        this.statusCode = statusCode;
        this.responseHeader = responseHeader;
        this.bodyContent = content;
    }

    public RequestResult(StatusCodeType statusCode, String content) {
        this.statusCode = statusCode;
        this.responseHeader = new HashMap<>();
        this.bodyContent = content.getBytes();
    }

    public void setResponseHeader(String key, String value) { this.responseHeader.put(key, value); }

    public HashMap<String, String> getResponseHeader() { return responseHeader; }

    public StatusCodeType getStatusCode() { return statusCode; }

    public byte[] getBodyContent() { return bodyContent; }
}
