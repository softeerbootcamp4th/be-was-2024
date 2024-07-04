package processor;

import type.StatusCodeType;
import webserver.RequestResult;

import java.util.HashMap;

public class RequestProcessor {
    private String method;
    private String path;
    private HashMap<String, String> query;

    RequestResult requestResult;

    public void init(String method, String path, HashMap<String, String> query) {
        this.method = method;
        this.path = path;
        this.query = query;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, String> getQuery() {
        return query;
    }

    public void setResult(StatusCodeType statusCode, byte[] content)  {
        this.requestResult = new RequestResult(statusCode, content);
    }

    public void setResult(StatusCodeType statusCode, String content) {
        setResult(statusCode, content.getBytes());
    }

    public RequestResult getResult() { return requestResult; }
}
