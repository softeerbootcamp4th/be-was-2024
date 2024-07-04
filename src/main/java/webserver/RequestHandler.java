package webserver;

import java.io.*;
import java.net.Socket;

import model.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParser;


public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String RESOURCE_PATH = "src/main/resources/static";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            //inputstream을 HttpRequest 객체로 변환
            HttpRequest httpRequest = RequestParser.convertInputStreamToHttpRequest(in);
            String contentType = RequestParser.parseContentTypeFromRequestPath(httpRequest.getPath());

            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(RESOURCE_PATH + httpRequest.getPath());

            System.out.println("@@@@@@"+RESOURCE_PATH + httpRequest.getPath());

            //io를 사용하여 파일 읽어오기
            InputStream fileInputStream = new FileInputStream(file);
            byte[] body = fileInputStream.readAllBytes();

            response200Header(dos, contentType,body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());

        }
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
            ;
            logger.info(dos.toString());
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
