package webserver.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int status;
    private byte[] body;
    private Map<String, String> headers;

    public HttpResponse(int status, byte[] body) throws IOException {
        this.status = status;
        this.body = body;
        this.headers = new HashMap<>();
    }

    public byte[] toByte(){

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.writeBytes(("HTTP/1.1 "+status+" OK \r\n").getBytes());

        for( Map.Entry<String, String> entry : headers.entrySet() ){
            String strKey = entry.getKey();
            String strValue = entry.getValue();
            outputStream.writeBytes( (strKey +": "+ strValue +"\r\n").getBytes());
        }
        outputStream.writeBytes("\r\n".getBytes());
        outputStream.write(body, 0, body.length);

        return outputStream.toByteArray();

    }

    public void addHeader(String key, String value){
        this.headers.put(key, value);
    }

}
