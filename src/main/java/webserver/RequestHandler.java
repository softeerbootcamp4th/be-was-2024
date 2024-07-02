package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import data.HttpRequestMessage;
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
            DataOutputStream dos = new DataOutputStream(out);
            String requestString = getRequestString(in);
            logger.debug(requestString);
            HttpRequestMessage httpRequestMessage = getHttpRequestMessage(requestString);
            File file = new File("src/main/resources/static" + httpRequestMessage.getUri());
            byte[] body = readAllBytesFromFile(file);
            response200Header(dos, httpRequestMessage.getUri().split("\\.")[1] ,body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getRequestString(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String tempStr;
        StringBuilder stringBuilder = new StringBuilder();
        while (!(tempStr = bufferedReader.readLine()).equals("")) {
            stringBuilder.append(tempStr);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private HttpRequestMessage getHttpRequestMessage(String requestMessage){
        String[] messageSplit = requestMessage.split("\n\n",2);
        String headerPart = messageSplit[0];
        String bodyPart = "";
        if (messageSplit.length > 1) {
            bodyPart = messageSplit[1];
        }

        String[] headerSplit = headerPart.split("\n",2);
        String startLine = headerSplit[0];

        String[] startLineSplit = startLine.split(" ");
        String method = startLineSplit[0];
        String uri = startLineSplit[1];
        String version = startLineSplit[2];

        String[] headerArray =  headerSplit[1].split("\n");
        Map<String, String> headers = new HashMap<>();
        for (String header : headerArray) {
            String[] headerParts = header.split(": ", 2);
            headers.put(headerParts[0], headerParts[1]);
        }

        return new HttpRequestMessage(method,uri,version,headers,bodyPart);
    }

    private byte[] readAllBytesFromFile(File file) throws IOException {
        byte[] bytes;
        try (FileInputStream fileInputStream = new FileInputStream(file)){
            bytes = fileInputStream.readAllBytes();
        }
        return bytes;
    }

    private void response200Header(DataOutputStream dos, String type, int lengthOfBodyContent) {
        String contentType = switch (type) {
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "html" -> "text/html";
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "ico" -> "image/x-icon";
            case "svg" -> "image/svg+xml";
            default -> "text/plain";
        };
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType +";charset=utf-8\r\n");
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
