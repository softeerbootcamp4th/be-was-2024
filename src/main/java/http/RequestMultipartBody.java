package http;

import java.util.HashMap;

public class RequestMultipartBody extends RequestBody{
    HashMap<String, String> headers;

    public RequestMultipartBody(HashMap<String, String> headers, byte[] body) {
        super(body);
        this.headers = headers;
    }

    public HashMap<String, String> getHeaders(){
        return headers;
    }

    public byte[] getBody(){
        return body;
    }

}
