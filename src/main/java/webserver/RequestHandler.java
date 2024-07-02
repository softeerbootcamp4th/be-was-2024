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

            InputStreamReader isr = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            logger.debug("request = " + line);
            String[] tokens = line.split(" ");
            String url = tokens[1];

            if (url.equals("/")) {
                byte[] body = "<h1>Hello World</h1>".getBytes();
                response200Header(dos, body.length);
                responseBody(dos, body);

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
                e.printStackTrace();
            }

            while (line.isEmpty()) {
                logger.debug("request = " + line);
                line = br.readLine();
            }

            response200Header(dos, body.length);
            responseBody(dos, body);

        } catch (IOException e) {
            logger.error(e.getMessage());
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
