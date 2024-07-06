package dto;

import constant.HttpMethod;
import exception.HttpRequestParsingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {
    private HttpMethod httpMethod;
    private String path;
    private Map<String, String> queryParams;
    private String extensionType;
    private Map<String, String> headers = new HashMap<>();

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Optional<String> getPath() {
        return Optional.ofNullable(path);
    }

    public Optional<Map<String, String>> getQueryParams() {
        return Optional.ofNullable(queryParams);
    }

    public Optional<String> getExtensionType() {
        return Optional.ofNullable(extensionType);
    }

    public Optional<String> getHeader(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    public void setHttpMethod(String httpMethod) {
        try{
            this.httpMethod = HttpMethod.valueOf(httpMethod);
        }
        catch (IllegalArgumentException e){
            throw new HttpRequestParsingException("Incorrect HttpMethod");
        }

    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public void setExtensionType(String extensionType) {
        this.extensionType = extensionType;
    }

    public void setHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }
}
