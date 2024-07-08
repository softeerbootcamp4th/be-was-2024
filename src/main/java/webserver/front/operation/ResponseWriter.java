package webserver.front.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.front.data.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class ResponseWriter {
    private final Logger logger = LoggerFactory.getLogger(ResponseWriter.class);
    public  void response(HttpResponse httpResponse, OutputStream out) {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] data = httpResponse.body;
        responseHeader(dos, httpResponse);
        responseBody(dos,data);

    }
    private void responseHeader(DataOutputStream dos, HttpResponse httpResponse) {
        try {

            dos.writeBytes("HTTP/"+httpResponse.getHttpVersion()+" "+
                            httpResponse.getStatusCode()+" "+
                            httpResponse.getStatusText()+" \r\n");
            dos.writeBytes("Content-Type: "+httpResponse.getContentType()+"; charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + httpResponse.getContentLength() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

