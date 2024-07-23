package processor;

import type.MIME;
import type.HTTPStatusCode;
import webserver.RequestInfo;
import webserver.RequestResult;

import java.util.HashMap;

public class RequestProcessor {
    public final static String STATIC_PATH = "./src/main/resources/static";

    public final static String CONTENT_CHARSET = ";charset=utf-8";

    private RequestInfo requestInfo;
    private HashMap<String, String> responseHeader = new HashMap<>();

    RequestResult requestResult;

    public void init(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public String methodPath() {
        return requestInfo.getMethod() + " " + requestInfo.getPath();
    }

    public String getMethod() {
        return requestInfo.getMethod();
    }

    public HashMap<String, String> getRequestHeaders() { return requestInfo.getHeader(); }

    public HashMap<String, String> getCookie() { return requestInfo.getCookie(); }

    public String getPath() {
        return requestInfo.getPath();
    }

    public HashMap<String, String> getQuery() {
        return requestInfo.getQuery();
    }

    public byte[] getBody() {
        return requestInfo.getBody();
    }

    public void insertToResponseHeader(String key, String value) {
        responseHeader.put(key, value);
    }

    public void insertContentTypeToHeader(String value) { responseHeader.put("Content-Type", value + CONTENT_CHARSET); }

    public void insertHTMLTypeToHeader() { insertContentTypeToHeader(MIME.HTML.getContentType()); }

    public HashMap<String, String> getResponseHeader() {
        return responseHeader;
    }

    public void setResult(HTTPStatusCode statusCode, HashMap<String, String> responseHeader, byte[] bodyContent)  {
        if (bodyContent.length > 0) responseHeader.put("Content-Length", Integer.toString(bodyContent.length));
        this.requestResult = new RequestResult(statusCode, responseHeader, bodyContent, null);
    }

    public void setResult(HTTPStatusCode statusCode, HashMap<String, String> responseHeader,
                          byte[] bodyContent, HashMap<String, String> bodyParams)  {
        this.requestResult = new RequestResult(statusCode, responseHeader, bodyContent, bodyParams);
    }

    public void setResult(HTTPStatusCode statusCode, HashMap<String, String> responseHeader, String bodyContent)  {
        setResult(statusCode, responseHeader, bodyContent.getBytes());
    }

    public void setResult(HTTPStatusCode statusCode, String bodyContent) {
        setResult(statusCode, new HashMap<>(), bodyContent.getBytes());
    }

    public RequestResult getResult() { return requestResult; }
}
