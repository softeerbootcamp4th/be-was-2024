package http;


import java.util.Map;
import java.util.TreeMap;

import static util.StringUtil.*;
import static util.StringUtil.Header.*;
import static util.HttpStatus.*;

public class HttpResponse {
    private int statusCode;
    private final Map<String, String> headers = new TreeMap<>();
    private byte[] body = null;

    public HttpResponse() {}

    public static HttpResponse error(int statusCode, String message) {
        HttpResponse response = new HttpResponse();
        byte[] body = ("<h1>"+message+"</h1>").getBytes();

        response.setBody(body);
        response.setStatusCode(statusCode);
        response.addHeader(CONTENT_TYPE, "text/html;charset=utf-8");
        response.addHeader(CONTENT_LENGTH, String.valueOf(body.length));
        return response;
    }

    public static HttpResponse redirect(String redirectUrl) {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(SC_FOUND);
        response.addHeader(LOCATION, redirectUrl);
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
    public byte[] getBody() {
        return body;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }
    public void setBody(byte[] body) {
        this.body = body;
    }

    public String headersToString() {
        StringBuilder sb = new StringBuilder()
               .append(SUPPORTED_HTTP_VERSION).append(BLANK)
               .append(statusCode).append(BLANK)
               .append(getStautusCodeString(statusCode)).append(CRLF);

        for (String key : headers.keySet()) {
            sb.append(key).append(COLON + BLANK).append(headers.get(key)).append(CRLF);
        }
        sb.append(CRLF);

        return sb.toString();
    }

}
