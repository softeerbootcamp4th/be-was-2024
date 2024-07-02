package webserver;

import Mapper.UrlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import byteReader.ByteReader;
import byteReader.ByteReaderMapper;
import returnType.ContentTypeFactory;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseMaker {

    private final Logger logger = LoggerFactory.getLogger(ResponseMaker.class);
    private final ByteReaderMapper byteReaderMapper = new ByteReaderMapper();
    private final ByteReader byteReader;
    private final UrlMapper urlMapper;
    private ContentTypeFactory contentTypeFactory;
    private String contentType;

    public ResponseMaker( ContentTypeFactory contentTypeFactory){
        this.contentTypeFactory = contentTypeFactory;
        this.urlMapper = new UrlMapper();
        this.byteReader = byteReaderMapper.returnByteReader(contentTypeFactory.getContentType());
    }
    public void makeResponse(HttpRequest httpRequest, OutputStream out) throws IOException {
        contentType = contentTypeFactory.getContentType();
        ByteReader byteReader = byteReaderMapper.returnByteReader(contentType);
        String finalUrl = urlMapper.getMappedUrl(httpRequest.getUrl());
        try{
            byte[] body = byteReader.readBytes(finalUrl);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, contentType,body.length);
            responseBody(dos, body);
        }
        catch (Exception e){
            e.printStackTrace();
        }
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
