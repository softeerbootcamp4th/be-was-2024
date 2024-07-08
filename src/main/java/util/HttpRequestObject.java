package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestObject {

    private String requestMethod;
    private String requestPath;
    private Map<String, String> requestParams;
    private String httpVersion;
    private Map<String, String> requestHeaders;
    private Map<String, String> requestBody;

    private HttpRequestObject(String requestMethod, String requestPath, Map<String, String> requestParams, String httpVersion) {
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.requestParams = requestParams;
        this.httpVersion = httpVersion;
        this.requestHeaders = new HashMap<>();
        this.requestBody = new HashMap<>();
    }

    public static HttpRequestObject from(String requestLine) {
        String[] requestLineElements = Arrays.stream(requestLine.split(StringUtil.SPACES)).map(String::trim).toArray(String[]::new);
        String requestMethod = requestLineElements[0];
        String[] requestURIElements = Arrays.stream(requestLineElements[1].split(StringUtil.QUESTION_MARK)).map(String::trim).toArray(String[]::new);
        String requestPath = requestURIElements[0];
        Map<String, String> requestParams = new HashMap<>();
        if(requestURIElements.length > 1) {
            String[] params = requestURIElements[1].split(StringUtil.AND);
            for (String param : params) {
                String[] keyValue = param.split(StringUtil.EQUAL);
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

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public Map<String, String> getRequestBody() {
        return requestBody;
    }

    public void putHeaders(String headerLine){
        if(headerLine.isEmpty()) return;
        headerLine = headerLine.replaceAll(StringUtil.SPACES, StringUtil.SPACE); // remove multiple spaces
        int idx = headerLine.indexOf(StringUtil.COLON);
        if(idx == -1) {
            throw new IllegalArgumentException("Header is invalid");
        }
        String[] header = {headerLine.substring(0, idx), headerLine.substring(idx + 1)};
        requestHeaders.put(header[0].trim(), header[1].trim());
    }

    public void putBody(String body){
        String[] bodyElements = body.split(StringUtil.AND);
        if(bodyElements.length == 0) throw new IllegalArgumentException("Body is empty");
        for (String bodyElement : bodyElements) {
            String[] keyValue = bodyElement.split(StringUtil.EQUAL);
            requestBody.put(keyValue[0], keyValue[1]);
        }
    }
}
