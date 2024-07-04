package webserver.http;

import webserver.http.enums.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse{
    private StatusCode statusCode;
    private Map<String, String> headers = new HashMap<String, String>();
    private byte[] body;


    public HttpResponse( int statusCode) {
        this.statusCode = StatusCode.valueOfCode(statusCode);
        if(this.statusCode == null) this.statusCode = StatusCode.CODE404;
    }


    public void addHeaders(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getHeader() {
        String header = statusCode.getStartline();
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            header += entry.getKey() + ": " + entry.getValue() + "\r\n";
        }
        header += "\r\n";
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
