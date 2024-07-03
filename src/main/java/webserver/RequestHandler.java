package webserver;

import java.io.*;
import java.net.Socket;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static db.Database.*;

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
            String filePath = "src/main/resources/static" + url[1];
            if (url[1].equals("/registration")) {
                filePath = "src/main/resources/static/registration/index.html";
            }else if(url[1].startsWith("/user/create")){
                int parameter = url[1].indexOf("?");
                String[] userInfo = url[1].substring(parameter+1).split("&");
                User user = new User(
                        userInfo[0].substring(userInfo[0].indexOf("=")+1),
                        userInfo[1].substring(userInfo[1].indexOf("=")+1),
                        userInfo[2].substring(userInfo[2].indexOf("=")+1),
                        userInfo[3].substring(userInfo[3].indexOf("=")+1)
                );
                addUser(user);
                String redirectUrl = "/index.html";
                dos.writeBytes("HTTP/1.1 302 Found\r\n");
                dos.writeBytes("Location: " + redirectUrl + "\r\n");
                dos.writeBytes("\r\n");
                return;
            }

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

    private byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[baos.size()+1];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        }
    }

    private String determineContentType(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        String extension = fileName.substring(dotIndex + 1).toLowerCase();

        switch (extension) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "json":
                return "application/json";
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "svg":
                return "image/svg+xml";
            case "ico":
                return "image/x-icon";
            default:
                return "application/octet-stream";
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
