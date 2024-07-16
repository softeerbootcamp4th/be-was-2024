package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import data.HttpRequestMessage;
import data.HttpResponseMessage;
import exception.BadMethodException;
import exception.BadUrlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                if (isRedirect(response)) redirect(dos, response.getHeaders());
                else response(response, dos);
            }
            catch (BadUrlException e) {
                HttpResponseMessage httpResponseMessage = UriMapper.errorResponseProcess("404");
                responseError(dos,httpResponseMessage);
            }
            catch (BadMethodException ex) {
                HttpResponseMessage httpResponseMessage = UriMapper.errorResponseProcess("405");
                responseError(dos,httpResponseMessage);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private static boolean isRedirect(HttpResponseMessage response) {
        return response.getStatusCode().startsWith("3");
    }

    private void redirect(DataOutputStream dos, Map<String,String> map) throws IOException {
        try {
            dos.writeBytes("HTTP/1.1 303 \r\n");
            for (Map.Entry<String, String> entry : map.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseError(DataOutputStream dos, HttpResponseMessage httpResponseMessage) throws IOException {
        try {
            Map<String, String> headers = httpResponseMessage.getHeaders();
            dos.writeBytes("HTTP/1.1 " + httpResponseMessage.getStatusCode() + " \r\n");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");
            dos.write(httpResponseMessage.getBody());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
