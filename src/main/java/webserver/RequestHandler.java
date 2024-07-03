package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final FileContentReader fileContentReader = FileContentReader.getInstance();
    private final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // Request
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // First Line
            String firstLine = br.readLine();
            logger.debug("HTTP Request First Line: {}", firstLine);

            // Headers
            Map<String, String> headers = httpRequestParser.parseRequestHeaders(br);

            // Path 및 Content-Type 지정
            String uri = httpRequestParser.parseRequestURI(firstLine);
            String contentType = httpRequestParser.parseRequestContentType(uri);


            // Response
            DataOutputStream dos = new DataOutputStream(out);

            // File reader
            byte[] body = fileContentReader.readStaticResource(uri);

            // Response
            response200Header(dos, contentType, body.length);
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
