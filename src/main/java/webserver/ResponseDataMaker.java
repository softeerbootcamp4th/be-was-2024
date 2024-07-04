package webserver;

import byteReader.ByteReader;

public class ResponseDataMaker {
    private ByteReader byteReader;
    private String message;
    public ResponseDataMaker(ByteReader byteReader, String message){
        this.byteReader = byteReader;
        this.message = message;
    }
    public HttpResponse getHttpResponse(){
        HttpResponse httpResponse = new HttpResponse("1.1",
                StatusCode.getCode(message),
                message,
                byteReader.readBytes(),
                byteReader.getContentType());
        return httpResponse;
    }
}
