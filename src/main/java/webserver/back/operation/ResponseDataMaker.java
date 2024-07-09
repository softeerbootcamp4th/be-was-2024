package webserver.back.operation;

import webserver.back.byteReader.ResponseErrorBody;
import webserver.back.data.StatusCode;
import webserver.back.byteReader.Body;
import webserver.front.data.HttpResponse;

public class ResponseDataMaker {
    public HttpResponse makeHttpResponse(Body body, String message){
        HttpResponse httpResponse = new HttpResponse("HTTP/1.1",
                StatusCode.getCode(message),
                message,
                body.makeBytes(),
                body.getContentType());
        return httpResponse;
    }
    public HttpResponse makeHttpResponse(Body body, String message, String location){
        HttpResponse httpResponse = makeHttpResponse(body, message);
        httpResponse.addLocation(location);
        return httpResponse;
    }
    public HttpResponse makeHttpResponseError(String message,String errorCause){
        Body body = new ResponseErrorBody(errorCause);
        return makeHttpResponse(body, message);
    }
}
