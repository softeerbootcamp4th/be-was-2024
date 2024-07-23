package data;

import java.util.Arrays;
import java.util.Map;

/**
 *Http 응답 메시지 객체
 */
public class HttpResponseMessage {
    private String statusCode;
    private Map<String,String> headers;
    private byte[] body;

    public HttpResponseMessage(String statusCode, Map<String, String> headers, byte[] body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
