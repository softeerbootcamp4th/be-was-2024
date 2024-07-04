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
