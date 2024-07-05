package webserver;

import java.io.*;
import java.net.Socket;

import model.HttpMethod;
import model.HttpRequest;
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

            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);

            // 로그 출력 후 요청 주소 반환
            HttpRequest httpRequest = requestLogging(br);
            HttpMethod method = httpRequest.method();
            String url = httpRequest.url();

            switch (method) {
                case GET -> HandleGetRequest.handler(url, out);
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }


    private HttpRequest requestLogging(BufferedReader br) throws IOException {

        String line = br.readLine();
        String[] request = line.split(" ");
        HttpMethod method = switch (request[0]) {
            case "GET" -> HttpMethod.GET;
            case "POST" -> HttpMethod.POST;
            case "PUT" -> HttpMethod.PUT;
            case "PATCH" -> HttpMethod.PATCH;
            case "DELETE" -> HttpMethod.DELETE;
            default -> throw new IllegalStateException("Unexpected value: " + request[0]);
        };

        String url = request[1];
        HttpRequest httpRequest = new HttpRequest(method, url);

        StringBuilder log = new StringBuilder();
        while (!line.isEmpty()) {
            log.append(line).append("\n");
            line = br.readLine();
        }

        logger.debug("\n\n***** REQUEST *****\n" + log);

        return httpRequest;
    }

}
