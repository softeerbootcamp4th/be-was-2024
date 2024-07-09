package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.mapper.MappingHandler;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequestParser requestParser = new HttpRequestParser();
            HttpRequest httpRequest = requestParser.parse(in);

            logger.debug("request line : {} {}", httpRequest.getMethod(), httpRequest.getUrl());

            // Use headers as needed
            for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
                logger.debug("header : {}={}", entry.getKey(), entry.getValue());
            }

            // Handle body if present
            byte[] body = httpRequest.getBody();
            if (body != null && body.length > 0) {
                logger.debug("body : {}", new String(body, "UTF-8"));
            }

            DataOutputStream dos = new DataOutputStream(out);
            RequestResponse requestResponse = new RequestResponse(httpRequest, dos);
            MappingHandler.mapRequest(httpRequest, requestResponse);

//            DataOutputStream dos = new DataOutputStream(out);
//            String filePath = MappingHandler.mapRequest(httpRequest, dos);
//
//            if(filePath != null){
//                File file = new File(filePath);
//                if (!file.exists()) {
//                    response404Header(dos);
//                    return;
//                }
//
//                byte[] fileBody = FileHandler.readFileToByteArray(file);
//                String contentType = FileHandler.determineContentType(file.getName());
//
//                response200Header(dos, fileBody.length, contentType);
//                responseBody(dos, fileBody);
//            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
            dos.writeBytes("<h1>404 Not Found</h1>");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            if (body != null) {
                dos.write(body, 0, body.length);
            }
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
