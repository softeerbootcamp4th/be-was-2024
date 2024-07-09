package webserver.http.response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Response {

    public final String CRLF = "\r\n";
    private final char SPACE = ' ';
    private final char COLON = ':';

    private final String version = "HTTP/1.1";
    private Status status;
    private Map<String, String> headers;
    private byte[] body;

    private Response(Builder builder){
        this.status = builder.status;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    static class Builder {

        private Status status;
        private Map<String, String> headers;
        private byte[] body;

        public Builder(Status status){
            this.status = status;
            headers = new HashMap<>();
            body = new byte[0];
        }

        public Builder headers(Map<String, String> headers){
            this.headers = headers;
            return this;
        }

        public Builder body(byte[] body){
            this.body = body;
            return this;
        }

        public Builder addHeader(String key, String value){
            this.headers.put(key, value);
            return this;
        }

        public Response build(){
            addHeader("Content-Length", String.valueOf(body.length));
            return new Response(this);
        }

    }

    public Status getStatus() {
        return status;
    }

    public String getHeaderValue(String key){
        return headers.get(key);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(version).append(SPACE).append(status.getCode()).append(SPACE).append(status.getMessage()).append(CRLF);
        for( Map.Entry<String, String> entry : headers.entrySet() ){
            String strKey = entry.getKey();
            String strValue = entry.getValue();
            sb.append(strKey).append(COLON).append(SPACE).append(strValue).append(CRLF);
        }
        sb.append(CRLF);
        sb.append(new String(body));
        sb.append(CRLF).append(CRLF);
        return sb.toString();
    }

    public byte[] toByte(){
        return this.toString().getBytes();
    }

    @Override
    public boolean equals(Object o){

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        if(!status.equals(response.status)) return false;
        if(!headers.equals(response.headers)) return false;
        if(!Arrays.equals(body, response.body)) return false;
        return true;
    }

}
