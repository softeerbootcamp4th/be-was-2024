package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);

            // 로그 출력 후 요청 주소 반환
            String url = RequestLogging.printRequest(br);
            String path = "./src/main/resources/static";

            if (url.equals("/")) {
                byte[] body = "<h1>Hello World</h1>".getBytes();
                response200Header(dos, body.length, "text/html");
                responseBody(dos, body);
                return;
            } else if (url.equals("/registration")) {
                url += "/index.html";
            }
            path += url;

            byte[] body = FileHandler.getFileContent(path);

            String[] tokens = url.split("\\.");
            String type = tokens[tokens.length - 1];

            response200Header(dos, body.length, getContentType(type));
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type + ";charset=utf-8\r\n");
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

    private String getContentType(String type){
        String contentType = "*/*";

        if (type.equals("html")) contentType =  "text./css";
        else if (type.equals("css")) contentType =  "text/css";
        else if (type.equals("js")) contentType =  "text/javascript";
        else if (type.equals("ico")) contentType =  "image/vnd.microsoft.icon";
        else if (type.equals("png")) contentType =  "image/png";
        else if (type.equals("jpg")) contentType =  "image/jpg";
        else if (type.equals("svg")) contentType =  "image/svg+xml";

        return contentType;
    }
}
