package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);

            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            String url = line.split(" ")[1];

            String request = "";
            while (!line.equals("")) {
                request += line + "\n";
                line = br.readLine();
            }
            logger.debug("\n\n***** REQUEST *****\n" + request);


            if (url.equals("/")) {
                byte[] body = "<h1>Hello World</h1>".getBytes();
                response200Header(dos, body.length, "text/html");
                responseBody(dos, body);
                return;
            }

            String path = "./src/main/resources/static" + url;
            String str = "";
            byte[] body = new byte[0];

            try {
                br = new BufferedReader(new FileReader(path));
                String fileLine = br.readLine();

                while (fileLine != null) {
                    str += fileLine;
                    fileLine = br.readLine();
                }
                body = str.getBytes();

            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            String[] tokens = url.split("\\.");
            String type = tokens[tokens.length - 1];

            if(type.equals("html")) response200Header(dos, body.length, "text/html");
            else if(type.equals("css")) response200Header(dos, body.length, "text/css");
            else if(type.equals("js")) response200Header(dos, body.length, "text/javascript");
            else if(type.equals("ico")) response200Header(dos, body.length, "image/vnd.microsoft.icon");
            else if(type.equals("png")) response200Header(dos, body.length, "image/png");
            else if(type.equals("jpg")) response200Header(dos, body.length, "image/jpg");
            else if(type.equals("svg")) response200Header(dos, body.length, "image/svg+xml");

            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + type + ";charset=utf-8\r\n");
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
