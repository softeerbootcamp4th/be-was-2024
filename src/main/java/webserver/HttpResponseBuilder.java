package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseBuilder {
    private DataOutputStream dos;
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public HttpResponseBuilder(DataOutputStream dos) {
        this.dos = dos;
    }

    public void response302header(String redirectpath){ //redirection
        try {
            dos.writeBytes("HTTP/1.1 302 \r\n");
            dos.writeBytes("Location: " + redirectpath+ "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("\r\n");

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void response200Header(int lengthOfBodyContent, String ContentType, byte[] body) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + ContentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
            responseBody(body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void response404Header() {
        try{
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.flush();
        } catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public void response404Header(byte[] body) {
        try{
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            responseBody(body);
        } catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    public void response405Header() {
        try{
            dos.writeBytes("HTTP/1.1 405 Unsupported content type\r\n");
            dos.flush();
        } catch (IOException e){
            logger.error(e.getMessage());
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
