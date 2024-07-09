package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import data.HttpRequestMessage;
import data.HttpResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequestMessage httpRequestMessage = HttpRequestParser.getHttpRequestMessage(in);
            logger.debug("Request Message: {}", httpRequestMessage);
            try {
                HttpResponseMessage response = UriMapper.mapUri(httpRequestMessage);
                if (response.getStatusCode().startsWith("3")) redirect(dos, response.getHeaders());
                else response(response, dos);
            }
            catch (Exception e) {
                response404(dos);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void redirect(DataOutputStream dos, Map<String,String> map) throws IOException {
        try {
            dos.writeBytes("HTTP/1.1 303 \r\n");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: 0" + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response(HttpResponseMessage response, DataOutputStream dos) throws IOException {
        response200Header(dos, response.getHeaders().get("Content-Type"),response.getBody().length);
        responseBody(dos, response.getBody());
    }

    private void response200Header(DataOutputStream dos, String contentType, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 NOT FOUND \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: 0 \r\n");
            dos.writeBytes("\r\n");
            dos.flush();
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
