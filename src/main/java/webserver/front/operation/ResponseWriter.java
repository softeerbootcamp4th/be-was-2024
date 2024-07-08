package webserver.front.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.front.data.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

public class ResponseWriter {
    private final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);
    private DataOutputStream dos;
    public  void response(HttpResponse httpResponse, OutputStream out) throws UnsupportedEncodingException {
        dos = new DataOutputStream(out);
        byte[] data = httpResponse.getBody();
//        System.out.println(new String(data,"UTF-8"));
//        printHeader(httpResponse);
        responseHeader(httpResponse);
        responseBody(data);
    }
    private void printHeader(HttpResponse httpResponse) {
        logger.debug(httpResponse.getFirstLine()+"\r\n");
        printData(httpResponse.getAdditionalHeader());
        printData(httpResponse.getRepresentationHeader());
    }
    private void printData(Map<String,String> data) {
        for(String key : data.keySet()){
            String value = data.get(key);
            logger.debug(key+": "+value);
        }
    }
    private void responseHeader( HttpResponse httpResponse) {
        try {
            dos.writeBytes(httpResponse.getFirstLine()+"\r\n");
            writeData(httpResponse.getAdditionalHeader());
            writeData(httpResponse.getRepresentationHeader());
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void writeData(Map<String,String> data) throws IOException {
        for(String key : data.keySet()){
            String value = data.get(key);
            dos.writeBytes(key+": "+value+"\r\n");
        }
    }
    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

