package webserver;

import byteReader.ByteReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponsePrinter {
    private final Logger logger = LoggerFactory.getLogger(ResponsePrinter.class);
    public  void response(ByteReader byteReader, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] data = byteReader.readBytes();
        response200Header(dos, byteReader.getContentType(),data.length);
        responseBody(dos,data);
    }
    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
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
