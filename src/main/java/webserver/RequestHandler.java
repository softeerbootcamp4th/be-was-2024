package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParser;


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
            //inputStream을 문자열로 변환
            BufferedReader buffer = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = buffer.readLine();
            String path = RequestParser.parseUriFromRequestHeaderStartLine(line);
            String contentType = RequestParser.parseContentTypeFromRequestHeaderStartLine(line);

            while (!line.isEmpty()) {
                logger.debug(line);
                line=buffer.readLine();
            }

            DataOutputStream dos = new DataOutputStream(out);
            File file = new File("src/main/resources/static" + path);

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
