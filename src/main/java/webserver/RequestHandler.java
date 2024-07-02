package webserver;

import java.io.*;
import java.net.Socket;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

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

            String[] splitURL = url.split("\\?");
            String requestUrl = splitURL[0];

            if (requestUrl.equals("/")) {
                path += "/index.html";
            } else if (requestUrl.equals("/registration")) {
                path += "/registration/index.html";
            } else if (requestUrl.equals("/user/create")) {
                String[] params = splitURL[1].split("&");

                String userId = params[0].split("=")[1];
                String password = params[1].split("=")[1];
                String name = params[2].split("=")[1];
                String email = params[3].split("=")[1];

                Database.addUser(new User(userId, password, name, email));

                String redirectResponse = "HTTP/1.1 302 Found\r\n" +
                        "Location: /\r\n" +
                        "Content-Length: 0\r\n" +
                        "\r\n";

                out.write(redirectResponse.getBytes());
            } else {
                path += url;
            }

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

    private String getContentType(String type) {
        return switch (type) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "text/javascript";
            case "ico" -> "image/vnd.microsoft.icon";
            case "png" -> "image/png";
            case "jpg" -> "image/jpg";
            case "svg" -> "image/svg+xml";
            default -> "*/*";
        };
    }
}
