package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import data.HttpRequestMessage;
import db.Database;
import model.User;
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
            String requestString = HttpRequestParser.getRequestString(in);
            logger.debug(requestString);
            HttpRequestMessage httpRequestMessage = HttpRequestParser.getHttpRequestMessage(requestString);
            String path = UriMapper.mapUri(httpRequestMessage);
            if (path.startsWith("redirect:")) redirect(dos,path.substring(9));
            else response200(path, dos);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void redirect(DataOutputStream dos, String location) throws IOException {
        try {
            dos.writeBytes("HTTP/1.1 303 \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Content-Type: " + findContentType("html") +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: 0" + "\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200(String path, DataOutputStream dos) throws IOException {
        byte[] body = FileUtil.readAllBytesFromFile(new File(path));
        response200Header(dos, path.split("\\.")[1] ,body.length);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, String ext, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + findContentType(ext) +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String findContentType(String ext){
        return switch (ext) {
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "html" -> "text/html";
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "ico" -> "image/x-icon";
            case "svg" -> "image/svg+xml";
            default -> "text/plain";
        };
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
