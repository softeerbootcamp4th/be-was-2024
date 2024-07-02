package webserver;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String BASE_DIR = "src/main/resources/static";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder requestHeader = new StringBuilder();

            //HTTP 요청 헤더를 읽고 모든 내용을 로거를 사용하여 출력
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                requestHeader.append(line).append("\n");
            }
            logger.debug("HTTP Request Headers:\n{}", requestHeader.toString());

            String[] requestLines = requestHeader.toString().split("\n");
            if (requestLines.length > 0) {
                String[] requestLineTokens = requestLines[0].split(" ");
                String method = requestLineTokens[0];
                String path = requestLineTokens[1];
                logger.debug("Request Method: {}, Path: {}", method, path);

                if ("/index.html".equals(path)) {
                    File file = new File(BASE_DIR + path);
                    if (file.exists()) {
                        byte[] body = readFileToByteArray(file);
                        DataOutputStream dos = new DataOutputStream(out);
                        response200Header(dos, body.length);
                        responseBody(dos, body);
                    } else {
                        DataOutputStream dos = new DataOutputStream(out);
                        byte[] body = "<h1>File Not Found</h1>".getBytes();
                        response404Header(dos, body.length);
                        responseBody(dos, body);
                    }
                } else {
                    DataOutputStream dos = new DataOutputStream(out);
                    byte[] body = "<h1>File Not Found</h1>".getBytes();
                    response404Header(dos, body.length);
                    responseBody(dos, body);
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private byte[] readFileToByteArray(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
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

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
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
