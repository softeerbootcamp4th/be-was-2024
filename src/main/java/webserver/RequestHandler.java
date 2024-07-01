package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
            // 모든 입력 데이터 (http) 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            // keep-alive 모드에서는 별도로 처리하지 않으면 null 반환하지 않고, 그냥 blocking 된다.
            // http header / body는 CR LF로 구분되므로, 공백 기준으로 읽지 않을 수도 없음.
            // readline은 /r or /n이 나오면 새로운 라인으로 판정.
            // => 두 방식 말고 http body 길이를 알 수 있는 방법이 필요하다.
            int body_length = 0;

            // 요청 라인
            // http_method, uri, http_version으로 구분됨
            String start_line = br.readLine();
            String[] line_info = start_line.split(" ");
            String http_method = line_info[0];
            String uri = line_info[1];
            String http_version = line_info[2];

            logger.debug(http_method);
            logger.debug(uri);
            logger.debug(http_version);

            List<String> header_lines = new ArrayList<>();
            // header을 먼저 처리
            String buffer;
            while (!(buffer = br.readLine()).isEmpty()) {
                header_lines.add(buffer);
            }
            Map<String, String> headers = new HashMap<>();
            for(var header_line : header_lines) {
                // 속성 값 부분에도 : 기호 존재 가능 => 2개만 나누기
                String[] parts = header_line.split(":", 2);
                headers.put(parts[0].trim(), parts[1].trim());
            }

            // 첫번째
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = "<h1> hello, world! </h1>".getBytes();

            response200Header(dos, body.length);
            responseBody(dos, body);
            logger.debug("send response");
        } catch (IOException e) {
            logger.error("{0}", e);
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
