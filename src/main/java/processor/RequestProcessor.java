package processor;

import type.StatusCodeType;
import webserver.RequestInfo;
import webserver.RequestResult;

import java.util.HashMap;

public class RequestProcessor {
    public  final static String STATIC_PATH = "./src/main/resources/static";
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

    public String getPath() {
        return requestInfo.getPath();
    }

    public String getContentType() {
        return requestInfo.getContentType();
    }

    public HashMap<String, String> getQuery() {
        return requestInfo.getQuery();
    }

    public String getBody() {
        return requestInfo.getBody();
    }

    public void insert2ResponseHeader(String key, String value) {
        responseHeader.put(key, value);
    }

    public HashMap<String, String> getResponseHeader() {
        return responseHeader;
    }

    public void setResult(StatusCodeType statusCode, HashMap<String, String> responseHeader, byte[] bodyContent)  {
        responseHeader.put("Content-Length", Integer.toString(bodyContent.length));
        this.requestResult = new RequestResult(statusCode, responseHeader, bodyContent);
    }

    public void setResult(StatusCodeType statusCode, HashMap<String, String> responseHeader, String bodyContent)  {
        setResult(statusCode, responseHeader, bodyContent.getBytes());
    }

    public void setResult(StatusCodeType statusCode, String bodyContent) {
        setResult(statusCode, new HashMap<>(), bodyContent.getBytes());
    }

    public RequestResult getResult() { return requestResult; }
}
