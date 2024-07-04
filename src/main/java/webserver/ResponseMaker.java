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
    private final ResponsePrinter responsePrinter;

    public ResponseMaker(UserMapper userMapper,ResponsePrinter responsePrinter){
        this.responseManager = new ResponseManager(userMapper);
        this.responsePrinter = responsePrinter;
    }
    public void makeResponse(HttpRequest httpRequest, OutputStream out) throws IOException {
        ByteReader byteReader = responseManager.getByte(httpRequest.getUrl());
        responsePrinter.response(byteReader,out);
    }
}
