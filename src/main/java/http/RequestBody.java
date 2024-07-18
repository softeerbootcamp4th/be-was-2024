package http;

public class RequestBody {
    byte[] body;

    public RequestBody(byte[] body){
        this.body = body;
    }

    public void setBody(byte[] body){
        this.body = body;
    }

    public byte[] getBody(){
        return body;
    }
}
