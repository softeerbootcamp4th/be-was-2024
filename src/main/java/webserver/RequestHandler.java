package webserver;

import java.io.*;
import java.net.Socket;
import java.util.*;

import http.HttpRequest;
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
            // 모든 입력 데이터 (http) 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            // 요청 라인 - http_method, uri, http_version으로 구분됨
            String requestLine = br.readLine();

            // 모든 헤더 라인 읽기
            // keep-alive 모드에서는 별도로 처리하지 않으면 null 반환하지 않고, 그냥 blocking 된다.
            // http header / body는 CR LF로 구분되므로, 공백 기준으로 읽지 않을 수도 없음.
            // readline은 /r or /n이 나오면 새로운 라인으로 판정.
            List<String> headerLines = new ArrayList<>();
            String buffer;
            while (!(buffer = br.readLine()).isEmpty()) {
                headerLines.add(buffer);
            }
            // body 부분은 나중에 추가적으로 확장
            HttpRequest request = new HttpRequest(requestLine, headerLines);

            logger.debug("http method: {}", request.getMethod());
            logger.debug("url: {}", request.getUrl());
            logger.debug("version: {}", request.getVersion());
            logger.debug("[headers]: {}", request.getHeaderMap());

            DataOutputStream dos = new DataOutputStream(out);
            // TODO StaticResourceMatcher로 리팩토링
            File targetFile = new File("./src/main/resources/static" + request.getUrl());

            byte[] body;
            if(targetFile.exists() && !targetFile.isDirectory()) {
                int bodyLength = (int)targetFile.length();

                body = new byte[bodyLength];
                try(FileInputStream fis = new FileInputStream(targetFile)) {
                    body = new byte[(int) targetFile.length()];
                    fis.read(body, 0, body.length);
                }
            } else {
                body = new byte[0];
            }

            response200Header(dos, body.length);
            responseBody(dos, body);
            logger.debug("send response");
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
