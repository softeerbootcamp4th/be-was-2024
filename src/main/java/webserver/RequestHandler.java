package webserver;

import java.io.*;
import java.net.Socket;
import java.io.File;

import http.HttpStatus;
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String requestURL = "";
            boolean isFirstLine = true;

            String line;
            StringBuilder sb = new StringBuilder("\n");
            while (!(line = bufferedReader.readLine()).isEmpty()){ //null check는 broken pipe 에러를 발생시킨다.
                if(isFirstLine){
                    String[] tokens = line.split(" ");
                    requestURL = tokens[1];
                    isFirstLine = false;
                }
                sb.append(line).append("\n");
            }
            logger.debug(sb.toString());

            responseByURL(new DataOutputStream(out), requestURL);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseByURL(DataOutputStream dos, String requestURL){
        File file = new File("src/main/resources/static" + requestURL);

        if(!file.exists()){
            byte[] body = "<h1>Page Not Found!</h1>".getBytes();
            responseHeader(dos, body.length, HttpStatus.SC_NOT_FOUND);
            responseBody(dos, body);
            return;
        }

        try(FileInputStream fis = new FileInputStream(file)){
            byte[] body = fis.readAllBytes();
            responseHeader(dos, body.length, HttpStatus.SC_OK);
            responseBody(dos, body);
        } catch (IOException e){
            logger.error(e.getMessage());
        }

    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, int statusCode){
        try {
            StringBuilder statusInfo = new StringBuilder()
                    .append(statusCode)
                    .append(" ")
                    .append(HttpStatus.getStautusString(statusCode))
                    .append("\r\n");

            dos.writeBytes("HTTP/1.1  "+ statusInfo.toString());
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        } catch (IllegalStateException ie){
            byte[] body = "<h1>Server Error</h1>".getBytes();
            responseHeader(dos, body.length, HttpStatus.SC_INTERNAL_SERVER_ERROR);
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
