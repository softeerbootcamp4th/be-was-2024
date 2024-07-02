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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();

            // 모든 요청 내용 읽기
            String startLine = br.readLine();

            String readLine = null;
            while (!(readLine = br.readLine()).isEmpty()) {
                sb.append(readLine);
            }

            // 읽은 내용 로깅
            String request = sb.toString();
            logger.debug(request);


            // 요청 URL 파싱
            String[] tokens = startLine.split(" ");
            String url = tokens[1];

            logger.info("요청 URL: " + url);

            // index.html 읽기
            String filePath = "/Users/jungwoo/Desktop/study/be-was-2024/src/main/resources/static" + url;
            logger.info("파일 경로: " + filePath);
            byte[] body = Files.readAllBytes(new File(filePath).toPath());

            DataOutputStream dos = new DataOutputStream(out);

//            byte[] body = "<h1>Hello World</h1>".getBytes();
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
