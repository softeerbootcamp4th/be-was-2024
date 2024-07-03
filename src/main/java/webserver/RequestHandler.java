package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.mapping.MappingHandler;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final FileContentReader fileContentReader = FileContentReader.getInstance();
    private final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();
    private final MappingHandler mappingHandler = MappingHandler.getInstance();

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
            String[] firstLines = httpRequestParser.parseRequestFirstLine(firstLine);
            String contentType = httpRequestParser.parseRequestContentType(firstLines[1]);


            // Response
            DataOutputStream dos = new DataOutputStream(out);

            // File reader
            byte[] body = fileContentReader.readStaticResource(firstLines[1]);
            int code = 200;
            Map<String, Object> response = null;

            if (body == null) {
                response = mappingHandler.mapping(firstLines[0], firstLines[1]);
                body = (byte[]) response.get("body");
                code = (int) response.get("code");
            }

            switch (code) {
                case 200:
                    response200Header(dos, contentType, body.length);
                    break;
                case 302:
                    String location = (String) response.get("location");
                    response302Header(dos, contentType, location);

            }

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

    private void response302Header(DataOutputStream dos, String contentType, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Location: " + location + "\r\n");
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
