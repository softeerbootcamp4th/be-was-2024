package dto;

import dto.enums.HttpMethod;

import java.util.Map;
import java.util.Optional;

public class HttpRequest {


    private final HttpMethod httpMethod;
    private final String path;
    private final Map<String, String> queryParams;
    private final String protocolVersion;
    private final Map<String, String> headers;
    private final byte[] body;

    private HttpRequest(HttpMethod httpMethod, String path, Map<String, String> queryParams, String protocolVersion,
                        Map<String, String> headers, byte[] body) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.queryParams = queryParams;
        this.protocolVersion = protocolVersion;
        this.headers = headers;
        this.body = body;
    }

    public static HttpRequest of(HttpMethod httpMethod, String path, Map<String, String> queryParams,
                                 String protocolVersion, Map<String, String> headers, byte[] body) {
        return new HttpRequest(httpMethod, path, queryParams, protocolVersion, headers, body);
    }

    public Optional<String> getSessionOrNull(){
        String cookie = this.getHeaders().get("Cookie");
        if(cookie.startsWith("sid=")){
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
}
