package http;

import java.util.HashMap;

public class HttpResponse {

    HttpStatus httpStatus;
    HashMap<String, String> headers;
    byte[] body;

    public HttpResponse(HttpStatus httpStatus, HashMap<String, String> headers, byte[] body) {
        this.httpStatus = httpStatus;
        this.headers = headers;
        this.body = body;
    }

    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 ").append(httpStatus.getStatus()).append(" ").append(httpStatus.getMessage()).append("\r\n");
        for (String key : headers.keySet()) {
            response.append(key).append(": ").append(headers.get(key)).append("\r\n");
        }
        response.append("\r\n");
        return response.toString();
    }
}
