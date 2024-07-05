package webserver.http;

import util.Parser;
import webserver.http.request.Request;
import webserver.http.response.ResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static util.Utils.getAllStrings;
import static webserver.http.request.RequestHandler.logger;

public class Processor {

    InputStream inputStream;
    OutputStream outputStream;

    public Processor(InputStream inputStream, OutputStream outputStream){
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void process() throws IOException {
        String request = convert(inputStream);
        logger.debug(request);

        Request httpRequest = Parser.parseRequest(request);

        if(httpRequest.isStatic()){
            ResponseHandler.responseStaticContents(outputStream, httpRequest);
            return;
        }

        ResponseHandler.responseDynamicContents(outputStream, httpRequest);
    }

    private String convert(InputStream inputStream) throws IOException {
        return getAllStrings(inputStream);
    }

}
