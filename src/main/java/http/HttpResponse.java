package http;

import java.util.Map;
import java.util.TreeMap;

public class HttpResponse {
    private int statusCode;
    private final String httpVersion = "HTTP/1.1";
    private final Map<String, String> headers = new TreeMap<>();
    private byte[] body = null;

    public HttpResponse() {}

    public static HttpResponse error(int statusCode, String message) {
        HttpResponse response = new HttpResponse();
        byte[] body = ("<h1>"+message+"</h1>").getBytes();

        response.setBody(body);
        response.setStatusCode(statusCode);
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.addHeader("Content-Length", String.valueOf(body.length));
        return response;
    }

    public static HttpResponse redirect(String redirectUrl) {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(HttpStatus.SC_FOUND);
        response.addHeader("Location", redirectUrl);
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
               .append(httpVersion).append(" ")
               .append(statusCode).append(" ")
               .append(HttpStatus.getStautusCodeString(statusCode)).append("\r\n");

        for(Map.Entry<String, String> entry : headers.entrySet()){
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        sb.append("\r\n");

        return sb.toString();
    }

}
