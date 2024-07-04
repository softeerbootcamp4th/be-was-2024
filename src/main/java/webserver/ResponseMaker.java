package webserver;

import Mapper.ResponseManager;
import Mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import byteReader.ByteReader;

import java.io.IOException;
import java.io.OutputStream;
public class ResponseMaker {

    private final Logger logger = LoggerFactory.getLogger(ResponseMaker.class);

    private final ResponseManager responseManager;
    private final ResponseWriter responseWriter;


    public ResponseMaker(UserMapper userMapper, ResponseWriter responseWriter){
        this.responseManager = new ResponseManager(userMapper);
        this.responseWriter = responseWriter;
    }
    public void makeResponse(HttpRequest httpRequest, OutputStream out) throws IOException {
        String statusCode;
        ByteReader byteReader = null;
        HttpResponse httpResponse = responseManager.getResponse(httpRequest.getUrl());
        responseWriter.response(httpResponse,out);
    }
}
