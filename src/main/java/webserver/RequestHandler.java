package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import db.Database;
import model.User;
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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            HttpRequestParser requestParser = new HttpRequestParser();
            requestParser.parse(in);

            String method = requestParser.getMethod();
            String url = requestParser.getUrl();
            Map<String, String> headers = requestParser.getHeaders();
            byte[] body = requestParser.getBody();

            logger.debug("request line : {} {}", method, url);

            // Use headers as needed
            headers.forEach((key, value) -> logger.debug("header : {}={}", key, value));

            // Handle body if present
            if (body != null && body.length > 0) {
                logger.debug("body : {}", new String(body, "UTF-8"));
            }

            DataOutputStream dos = new DataOutputStream(out);
            String filePath = AddressHandler.getFilePath(requestParser, dos);

            File file = new File(filePath);
            if (!file.exists()) {
                response404Header(dos);
                return;
            }

            for(User u :Database.findAll()){
                System.out.println(u);
            }

            byte[] fileBody = FileHandler.readFileToByteArray(file);
            String contentType = FileHandler.determineContentType(file.getName());

            response200Header(dos, fileBody.length, contentType);
            responseBody(dos, fileBody);
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
