package util;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestObject {

    private String requestMethod;
    private String requestPath;
    private Map<String, String> requestParams;
    private String httpVersion;

    private HttpRequestObject(String requestMethod, String requestPath, Map<String, String> requestParams, String httpVersion) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.requestParams = requestParams;
        this.httpVersion = httpVersion;
    }

    public static HttpRequestObject from(String requestLine) {
        String[] requestLineElements = requestLine.split(" ");
        String requestMethod = requestLineElements[0];
        String[] requestURIElements = requestLineElements[1].split("\\?"); // requestURI[0]: path, requestURI[1]: params
        String requestPath = requestURIElements[0];
        Map<String, String> requestParams = new HashMap<>();
        if(requestURIElements.length > 1) {
            String[] params = requestURIElements[1].split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                requestParams.put(keyValue[0], keyValue[1]);
            }
        }
        String requestVersion = requestLineElements[2];
        return new HttpRequestObject(requestMethod, requestPath, requestParams, requestVersion);
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
