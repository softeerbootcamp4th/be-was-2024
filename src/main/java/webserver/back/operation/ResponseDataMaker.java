package webserver.back.operation;

import webserver.back.data.StatusCode;
import webserver.back.byteReader.ByteReader;
import webserver.front.data.HttpResponse;

public class ResponseDataMaker {
    public HttpResponse makeHttpResponse(ByteReader byteReader, String message){
        HttpResponse httpResponse = new HttpResponse("HTTP/1.1",
                StatusCode.getCode(message),
                message,
                byteReader.readBytes(),
                byteReader.getContentType());
        return httpResponse;
    }
    public HttpResponse makeHttpResponse(ByteReader byteReader, String message,String location){
        HttpResponse httpResponse = makeHttpResponse(byteReader, message);
        httpResponse.addLocation(location);
        return httpResponse;
    }
}
