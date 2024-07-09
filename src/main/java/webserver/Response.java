package webserver;

import handler.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);

    private static final ResourceHandler resourceHandler = new ResourceHandler();

    public void runGetResponse() {

    }

    public void runPostResponse() {

    }

    public void response(String url, DataOutputStream dos) throws IOException {
        // url로부터 html파일을 byte array로 읽어오기
        byte[] body = resourceHandler.getByteArray(url);

        response200Header(dos, body.length, resourceHandler.getContentType(url));
        responseBody(dos, body);
    }

    public void redirect(String url, DataOutputStream dos, int redirectionCode)  throws IOException {
        if (redirectionCode == 301) {
            response301Header(dos, url);
        } else if (redirectionCode == 302) {
            response302Header(dos, url);
        } else {
            byte[] body = resourceHandler.getByteArray(url);
            response404Header(dos, body.length);
            responseBody(dos, body);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response301Header(DataOutputStream dos, String newLocation) {
        try {
            dos.writeBytes("HTTP/1.1 301 Moved Permanently \r\n");
            dos.writeBytes("Location: " + newLocation + "\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String newLocation) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + newLocation + "\r\n");
            dos.writeBytes("Content-Length: 0\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html\r\n");
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
