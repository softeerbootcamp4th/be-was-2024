package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Callable<String> {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public String call() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            Request request = new Request(br);
            DataOutputStream dos = new DataOutputStream(out);
            FileInputStream fis = new FileInputStream("src/main/resources/static" + request.getPath());
            byte[] body = fis.readAllBytes();
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return "hello";
    }

    private static Map<String, String> clientHeaderParser(InputStream in) throws IOException {
        Map<String, String> httpHeaders = new ConcurrentHashMap<>();
//        for(String line: headerLines) {
//            StringTokenizer headerToken = new StringTokenizer(line, ": ");
//            String headerName = headerToken.nextToken();
//            String headerValue = headerToken.nextToken();
//            httpHeaders.put(headerName, headerValue);
//        }
        return httpHeaders;
    }



    private String readString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
            if (inputStream.available() == 0) {
                break;
            }
        }
        return new String(byteArrayOutputStream.toByteArray(), StandardCharsets.UTF_8);
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
