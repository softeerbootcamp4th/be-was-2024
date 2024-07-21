package dto;

import constant.HttpMethod;
import exception.InvalidHttpRequestException;

import java.util.*;

public class HttpRequest {
    private static final String COOKIE_HEADER = "Cookie";
    private static final String COOKIE_NAME_SESSION_ID = "sessionId";
    private static final String COOKIE_NAME_REDIRECT = "redirect";

    private HttpMethod httpMethod;
    private String path;
    private Integer pathVariable;
    private Map<String, String> queryParams;
    private String extensionType;
    private Map<String, List<String>> headers;
    private byte[] body;

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

    public Optional<byte[]> getBody() {
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

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Optional<String> getSessionId(){
        if(headers == null)
            return Optional.empty();

        List<String> cookies = headers.get(COOKIE_HEADER);
        if(cookies!=null){
            for(String cookie : cookies){
                if(cookie.contains(COOKIE_NAME_SESSION_ID)){
                    String sessionId = cookie.substring((COOKIE_NAME_SESSION_ID + "=").length());
                    return Optional.of(sessionId);
                }
            }
        }

        return Optional.empty();
    }

    public Optional<String> getRedirectUrl(){
        if(headers == null)
            return Optional.empty();

        List<String> cookies = headers.get("Cookie");
        for(String cookie : cookies){
            if(cookie.contains(COOKIE_NAME_REDIRECT)){
                String redirectUrl = cookie.substring((COOKIE_NAME_REDIRECT + "=").length());
                return Optional.of(redirectUrl);
            }
        }
        return Optional.empty();
    }

    public Optional<Integer> getPathVariable() {
        return Optional.ofNullable(pathVariable);
    }

    public void setPathVariable(Integer pathVariable) {
        this.pathVariable = pathVariable;
    }
}
