package http;

import java.util.HashMap;

public class HttpResponse {

    private static final String HTTP_VERSION = "HTTP/1.1 ";
    HttpStatus httpStatus;
    HashMap<String, String> headers = new HashMap<>();
    byte[] body;

    /// 필수적인 필드값은 생성자에 넣어서 생성되도록 -> HttpStatus 같은 경우는 무조건 있어야 함, final 붙여주기
    public String getStatusLine(){
        return HTTP_VERSION + httpStatus.getStatus() + " " + httpStatus.getMessage() + "\r\n";
    }

    public String getHeader(String key){
        return headers.get(key);
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
