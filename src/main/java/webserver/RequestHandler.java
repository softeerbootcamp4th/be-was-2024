package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private FileContentReader fileContentReader;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // Request
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            Map<String, String> headers = new HashMap<>();

            line = br.readLine();
            String uri = HttpRequestParser.parseRequestURI(line);
            String extension = HttpRequestParser.parseRequestContentType(uri);


            String contentType = ContentType.html.getContentType();
            if (extension != null) {
                contentType = ContentType.valueOf(extension).getContentType();
            }

            requestBuilder.append(line).append("\n");

            // 요청 메시지를 한 줄씩 읽어 StringBuilder 에 추가
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append("\n");
                int index = line.indexOf(':');
                if (index > 0) {
                    String headerName = line.substring(0, index).trim();
                    String headerValue = line.substring(index + 1).trim();
                    headers.put(headerName, headerValue);
                }
            }

//            logger.debug("HTTP Request : {}", requestBuilder);

            // Response

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = FileContentReader.readStaticResource(uri);

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
