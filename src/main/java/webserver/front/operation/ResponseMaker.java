package webserver.front.operation;

import webserver.back.operation.ResponseManager;
import webserver.back.operation.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.front.data.HttpRequest;
import webserver.front.data.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class ResponseMaker {

    private final Logger logger = LoggerFactory.getLogger(ResponseMaker.class);

    private final ResponseManager responseManager;
    private final ResponseWriter responseWriter;


    public ResponseMaker(UserMapper userMapper, ResponseWriter responseWriter){
        this.responseManager = new ResponseManager(userMapper);
        this.responseWriter = responseWriter;
    }
    public void makeResponse(HttpRequest httpRequest, OutputStream out) throws IOException {
        HttpResponse httpResponse = responseManager.getResponse(httpRequest.getUrl());
        responseWriter.response(httpResponse,out);
    }
}
