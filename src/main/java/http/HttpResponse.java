package http;

import java.util.HashMap;

public class HttpResponse {

    private static final String HTTP_VERSION = "HTTP/1.1 ";
    HttpStatus httpStatus;
    HashMap<String, String> headers = new HashMap<>();
    byte[] body;


    public String getStatusLine(){
        return HTTP_VERSION + httpStatus.getStatus() + " " + httpStatus.getMessage() + "\r\n";
    }

    public String getHeaders(){
        StringBuilder header = new StringBuilder();
        for (String key : headers.keySet()) {
            header.append(key).append(": ").append(headers.get(key)).append("\r\n");
        }
        header.append("\r\n");
        return header.toString();
    }

    public byte[] getBody(){
        return body;
    }

    public HttpResponse addStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        return this;
    }

    public HttpResponse addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpResponse addBody(byte[] body) {
        this.body = body;
        return this;
    }
}
