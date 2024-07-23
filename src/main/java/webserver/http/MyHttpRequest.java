package webserver.http;

import webserver.enums.HttpMethod;

import java.util.Map;

public class MyHttpRequest {
    /**
     * Request-Line = Method SP Request-URI SP HTTP-Version CRLF
     * <p>
     * Request-URI = path [ "?" query ]
     */
    private final HttpMethod method;
    private final String path;
    private final Map<String, String> query;
    private final String version;

    /**
     * Headers
     * <p>
     * message-header = field-name ":" [ field-value ]
     */
    private final Map<String, String> headers;

    /**
     * message-body
     * <p>
     */
    private final byte[] body;

    public MyHttpRequest(HttpMethod method, String path, Map<String, String> query, String version, Map<String, String> headers, byte[] body) {
        this.method = method;
        this.path = path;
        this.query = query;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "{ method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", query=" + query +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", body= { " + (body != null ? new String(body) : "null") +
                " }, }";
    }
}
