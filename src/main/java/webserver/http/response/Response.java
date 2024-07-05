package webserver.http.response;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private int status;
    private byte[] body;
    private Map<String, String> headers;

    public Response(int status, byte[] body) {
        this.status = status;
        this.body = body;
        this.headers = new HashMap<>();
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

    public void addHeader(String key, String value){
        this.headers.put(key, value);
    }

}
