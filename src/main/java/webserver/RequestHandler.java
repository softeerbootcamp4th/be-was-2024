package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

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

        URLParser urlParser = new URLParser();
        ResourceHandler resourceHandler = new ResourceHandler();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            // InputStream을 BufferedReader로 변환
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // 읽어들인 InputStream 모두 출력
            String line; // 읽어들일 라인
            String url = ""; // 요청 파일
            while (!(line = br.readLine()).isEmpty()) {
                // 요청이 GET일 경우
                if (urlParser.getHttpMethod(line).equals("GET")) {
                    logger.debug(line);
                    // 요청 파일 받아오기
                    url = urlParser.getGetURL(line);
                }
            }

            DataOutputStream dos = new DataOutputStream(out);

            // url로부터 html파일을 byte array로 읽어오기
            byte[] body = resourceHandler.getByteArray(url);

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
}
