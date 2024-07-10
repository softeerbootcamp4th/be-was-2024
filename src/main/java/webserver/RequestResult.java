package webserver;

import type.HTTPStatusCode;

import java.util.HashMap;

public class RequestResult {
    private HTTPStatusCode statusCode;
    private HashMap<String, String> responseHeader;
    private HashMap<String, String> bodyParams;
    private byte[] bodyContent;

    public RequestResult(HTTPStatusCode statusCode, HashMap<String, String> responseHeader,
                         byte[] content, HashMap<String, String> bodyParams) {
        this.statusCode = statusCode;
        this.responseHeader = responseHeader;
        this.bodyContent = content;
        this.bodyParams = bodyParams;
    }

    public RequestResult(HTTPStatusCode statusCode, String content) {
        this.statusCode = statusCode;
        this.responseHeader = new HashMap<>();
        this.bodyContent = content.getBytes();
    }

    public void setResponseHeader(String key, String value) { this.responseHeader.put(key, value); }

    public HashMap<String, String> getResponseHeader() { return responseHeader; }

    public HTTPStatusCode getStatusCode() { return statusCode; }

    public byte[] getBodyContent() { return bodyContent; }

    public HashMap<String, String> getBodyParams() { return bodyParams; }
}
