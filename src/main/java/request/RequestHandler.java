package request;

import java.io.*;
import java.net.Socket;

import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;
    private final RequestParser requestParser;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        requestParser = new RequestParser();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpResponse response = requestParser.ParsingRequest(in);
            // 이친구 생성할 필요 없음 -> static, 생성자에서 빼기, main에서 주입해주기, 지금은 스레드마다 생성되므로 낭비

            DataOutputStream dos = new DataOutputStream(out);
            dos.writeBytes(response.getStatusLine());
            dos.writeBytes(response.getHeaders());
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
