package http;

import java.util.HashMap;
import java.util.Optional;

public class HttpRequest {

    HttpMethod httpMethod;
    String requestUrl;
    String version;
    HashMap<String, String> headers = new HashMap<>();
    byte[] body;


    public void setStartLine(HttpMethod httpMethod, String requestUrl, String version) {
        this.httpMethod = httpMethod;
        this.requestUrl = requestUrl;
        this.version = version;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public HttpMethod getHttpMethod() {
        return this.httpMethod;
    }

    public String getRequestUrl() {
        return this.requestUrl;
    }

    public Optional<String> getHeaders(String key) {
        return Optional.ofNullable(headers.get(key));
    }

    public byte[] getBody() {
        return body;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpMethod + " " + requestUrl + " " + version + "\n");
        for (String s : headers.keySet()) {
            sb.append(s + ": " + headers.get(s) + "\n");
        }
        if (body != null) {
            sb.append("body : ");
            for (byte b : body) {
                sb.append((char) b);
            }
        }
        return sb.toString();
    }
}
