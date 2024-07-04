package webserver;

import java.io.*;
import java.net.Socket;
import java.io.File;

import http.HttpRequest;
import http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private final String basePath = "src/main/resources/static";

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = createHttpRequest(in);
            logger.debug(httpRequest.toString());

            response(new DataOutputStream(out), httpRequest);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpRequest createHttpRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

        HttpRequest httpRequest = null;
        boolean isStartLine = true;

        String line;
        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) { //null check only는 broken pipe 에러를 발생시킨다.
            if (isStartLine) {
                String[] tokens = line.split(" ");
                httpRequest = new HttpRequest(tokens[0], tokens[1], tokens[2]);
                isStartLine = false;
                continue;
            }

            int colonIndex = line.indexOf(':');
            if (colonIndex == -1) {
                throw new IOException("Invalid HTTP header: " + line);
            }
            String key = line.substring(0, colonIndex).trim();
            String value = line.substring(colonIndex + 1).trim();
            httpRequest.addHeader(key, value);
        }

        return httpRequest;
    }

    private void response(DataOutputStream dos, HttpRequest httpRequest) {
        File file = new File(basePath + httpRequest.getUrl());

        if (!file.exists()) {
            //이 부분에서 보기 좋은 에러페이지 html 파일을 읽어들여서 내보내면 더 좋을듯
            byte[] body = "<h1>Page Not Found!</h1>".getBytes();
            responseHeader(dos, body.length, HttpStatus.SC_NOT_FOUND, "text/html;charset=utf-8");
            responseBody(dos, body);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] body = fis.readAllBytes();
            responseHeader(dos, body.length, HttpStatus.SC_OK, httpRequest.getContentType());
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, int statusCode, String contentType) {
        try {
            StringBuilder statusInfo = new StringBuilder()
                    .append(statusCode)
                    .append(" ")
                    .append(HttpStatus.getStautusCodeString(statusCode))
                    .append("\r\n");

            dos.writeBytes("HTTP/1.1  " + statusInfo.toString());
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (IllegalStateException ie) {
            logger.error(ie.getMessage());
            byte[] body = "<h1>Server Error</h1>".getBytes();
            responseHeader(dos, body.length, HttpStatus.SC_INTERNAL_SERVER_ERROR, "text/html;charset=utf-8");
            responseBody(dos, body);
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
