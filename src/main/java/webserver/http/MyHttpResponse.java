package webserver.http;

import webserver.enums.ContentType;
import webserver.enums.HttpStatus;

import java.util.HashMap;
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

    public MyHttpResponse(HttpStatus httpStatus, Map<String, String> headers) {
        this(httpStatus, headers, new byte[0]);
    }

    public MyHttpResponse(HttpStatus httpStatus) {
        this(httpStatus, new HashMap<>(), new byte[0]);
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
        this.headers.put("Content-Length", String.valueOf(body.length));
        this.body = body;
    }

    public void addContentType(String contentType) {
        int extensionIndex = contentType.lastIndexOf(".");

        if (extensionIndex != -1) {
            String extension = contentType.substring(extensionIndex + 1);
            headers.put("Content-Type", ContentType.valueOf(extension.toUpperCase()).getContentType());
        }
    }

    @Override
    public String toString() {
        return "{ httpStatus + " + httpStatus +
                ", headers=" + headers +
                ", body=" + body +
                " }";
    }
}
