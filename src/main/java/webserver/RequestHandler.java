package webserver;

import java.io.*;
import java.net.Socket;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static db.Database.*;
import static webserver.FileHandler.*;
import static webserver.AddressHandler.*;

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
            String line = br.readLine();

            if (line == null || line.isEmpty()) {
                return;
            }

            logger.debug("request line : {}", line);

            String[] url = line.split(" ");
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                logger.debug("header : {}", line);
            }

            DataOutputStream dos = new DataOutputStream(out);

            String filePath = getFilePath(url[1], dos);

            File file = new File(filePath);
            if (!file.exists()) {
                response404Header(dos);
                return;
            }

            for(User u : findAll()){
                System.out.println(u);
            }

            byte[] body = readFileToByteArray(file);
            String contentType = determineContentType(file.getName());

            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void response404Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
            dos.writeBytes("<h1>404 Not Found</h1>");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
