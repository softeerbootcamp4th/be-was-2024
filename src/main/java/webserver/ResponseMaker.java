package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import byteReader.ByteReader;
import byteReader.SimpleByteReaderFactory;
import returnType.ContentTypeFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseMaker {

    private final Logger logger = LoggerFactory.getLogger(ResponseMaker.class);
    private final SimpleByteReaderFactory simpleByteReaderFactory = new SimpleByteReaderFactory();
    private final ByteReader byteReader;
    private ContentTypeFactory contentTypeFactory;
    private String contentType;

    public ResponseMaker( ContentTypeFactory contentTypeFactory){
        this.contentTypeFactory = contentTypeFactory;
        this.byteReader = simpleByteReaderFactory.returnByteReader(contentTypeFactory.getContentType());
    }
    public void makeResponse(HttpRequest httpRequest, OutputStream out) throws IOException {
        contentType = contentTypeFactory.getContentType();
        ByteReader byteReader = simpleByteReaderFactory.returnByteReader(contentType);

        if(byteReader==null) throw new IOException();
        byte[] body = byteReader.readBytes(httpRequest.getUrl());
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, contentType,body.length);
        responseBody(dos, body);

    }


    private void response200Header(DataOutputStream dos, String contentType,int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+contentType+";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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
