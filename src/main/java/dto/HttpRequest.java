package dto;

import dto.enums.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {

    private final HttpMethod httpMethod;
    private final String path;
    private final Map<String, String> queryParams;
    private final String protocolVersion;
    private final Map<String, String> headers;
    private final byte[] body;
    private final Map<String, String> formFields;
    private final Map<String, byte[]> fileMap;

    private HttpRequest(HttpMethod httpMethod, String path, Map<String, String> queryParams, String protocolVersion,
                        Map<String, String> headers, byte[] body, Map<String, String> formFields, Map<String, byte[]> fileMap) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.queryParams = queryParams;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        this.body = body;
        this.formFields = formFields;
        this.fileMap = fileMap;
    }

    public static HttpRequest of(HttpMethod httpMethod, String path, Map<String, String> queryParams,
                                 String protocolVersion, Map<String, String> headers, byte[] body) {
        return new HttpRequest(httpMethod, path, queryParams, protocolVersion, headers, body, new HashMap<>(), new HashMap<>());
    }

    public static HttpRequest of(HttpMethod httpMethod, String path, Map<String, String> queryParams,
                                 String protocolVersion, Map<String, String> headers,
                                 Map<String, String> formFields, Map<String, byte[]> fileMap) {
        return new HttpRequest(httpMethod, path, queryParams, protocolVersion, headers, null, formFields, fileMap);
    }

    public Optional<String> getSessionOrNull() {
        String cookie = this.getHeaders().get("Cookie");
        if (cookie != null && cookie.startsWith("sid=")) {
            String sessionId = cookie.substring("sid=".length());
            return Optional.of(sessionId);
        }
        return Optional.empty();
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public Map<String, String> getFormFields() {
        return formFields;
    }

    public Map<String, byte[]> getFileMap() {
        return fileMap;
    }
}
