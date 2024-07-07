package webserver.http.response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Response {

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
            addHeader("Content-length", String.valueOf(body.length));
            return new Response(this);
        }

    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ").append(status).append(" OK \r\n");
        for( Map.Entry<String, String> entry : headers.entrySet() ){
            String strKey = entry.getKey();
            String strValue = entry.getValue();
            sb.append(strKey).append(": ").append(strValue).append("\r\n");
        }
        sb.append("\r\n");
        sb.append(new String(body));
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
