package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;
import util.HttpRequestObject;
import util.HttpRequestParser;
import util.ContentType;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            // 요청 Header 출력
            String line = br.readLine(); // Request Line (ex: "GET /index.html HTTP/1.1")
            String[] requestLineElements = HttpRequestParser.parseRequestLine(line);
            while(!line.isEmpty()) {
                logger.debug("header: {}", line);
                line = br.readLine();
            }

            // HttpRequestObject 생성
            HttpRequestObject httpRequestObject = HttpRequestObject.from(requestLineElements);

            // 데이터 입출력 및 응답
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = FileUtil.readBytesFromFile(httpRequestObject.getRequestURI());
            response200Header(dos, body.length, httpRequestObject.getRequestURI().split("\\.")[1]);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String extension) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + ContentType.getType(extension) + "\r\n");
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
