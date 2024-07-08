package webserver.http;

import webserver.http.enums.StatusCode;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse{
    private StatusCode statusCode;
    private Map<String, String> headers;
    private byte[] body;

    public String getHeader() {
        StringBuilder header = new StringBuilder();
        header.append(statusCode.getStartline());
        for(Map.Entry<String, String> entry : headers.entrySet()) {
            header.append(entry.getKey());
            header.append(": ");
            header.append(entry.getValue());
            header.append("\r\n");
        }
        header.append("\r\n");
        return header.toString();
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public byte[] getBody() {
        return body;
    }


    private HttpResponse(ResponseBuilder responseBuilder) {
        this.statusCode = responseBuilder.statusCode;
        this.headers = responseBuilder.headers;
        this.body = responseBuilder.body;
    }


    public static class ResponseBuilder{
        private StatusCode statusCode;
        private byte[] body;
        private Map<String, String> headers = new HashMap<>();

        public ResponseBuilder(int statusCode){
            this.statusCode = StatusCode.valueOfCode(statusCode);
            if(this.statusCode == null) this.statusCode = StatusCode.CODE404;
        }

        public ResponseBuilder addheader(String key, String value){
            headers.put(key, value);
            return this;
        }

        public ResponseBuilder setBody(byte[] body){
            this.body = body;
            return this;
        }

        public HttpResponse build(){
            return new HttpResponse(this);
        }
    }
}
