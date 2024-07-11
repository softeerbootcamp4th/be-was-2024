package dto;

import constant.HttpMethod;
import exception.InvalidHttpRequestException;
import session.Session;

import java.util.*;

public class HttpRequest {
    private HttpMethod httpMethod;
    private String path;
    private Map<String, String> queryParams;
    private String extensionType;
    private Map<String, List<String>> headers;
    private String body;

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

    public Optional<List<String>> getHeader(String key) {
        if(headers == null)
            return Optional.empty();
        return Optional.ofNullable(headers.get(key));
    }

    public Optional<String> getBody() {
        return Optional.ofNullable(body);
    }

    public void setHttpMethod(String httpMethod) {
        try{
            this.httpMethod = HttpMethod.valueOf(httpMethod);
        }
        catch (IllegalArgumentException e){
            throw new InvalidHttpRequestException("Incorrect HttpMethod");
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
        if(headers == null)
            headers = new HashMap<>();
        if(!headers.containsKey(headerName))
            headers.put(headerName, new ArrayList<>());
        headers.get(headerName).add(headerValue);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Optional<String> getSessionId(){
        if(headers == null)
            return Optional.empty();

        List<String> cookies = headers.get("Cookie");
        for(String cookie : cookies){
            if(cookie.startsWith("sessionId=")){
                String sessionId = cookie.substring("sessionId=".length());
                return Optional.of(sessionId);
            }
        }

        return Optional.empty();
    }
}
