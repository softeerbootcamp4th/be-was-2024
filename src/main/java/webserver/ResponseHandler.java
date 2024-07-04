package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    private static final URLParser urlParser = new URLParser();
    private static final ResourceHandler resourceHandler = new ResourceHandler();

    public void runGetResponse(String requestLine, DataOutputStream dos) throws IOException {
        String url = urlParser.getURL(requestLine);

        // url로부터 html파일을 byte array로 읽어오기
        byte[] body = resourceHandler.getByteArray(url);

        response200Header(dos, body.length, resourceHandler.getContentType(url));
        responseBody(dos, body);
    }

    public void runPostResponse() {

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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
