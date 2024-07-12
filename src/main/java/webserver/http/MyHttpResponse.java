package webserver.http;

import webserver.enums.HttpStatus;

import java.util.Map;

public class MyHttpResponse {
    /**
     * Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
     */
    private final String version = "HTTP/1.1";
    private HttpStatus httpStatus;
    /**
     * Headers
     * <p>
     * message-header = field-name ":" [ field-value ]
     */
    private Map<String, String> headers;

    /**
     * message-body
     * <p>
     */
    private byte[] body;

    public MyHttpResponse() {
    }

    public MyHttpResponse(HttpStatus httpStatus, Map<String, String> headers, byte[] body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public String getVersion() {
        return version;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{ httpStatus + " + httpStatus +
                ", headers=" + headers +
                ", body=" + body +
                " }";
    }
}
