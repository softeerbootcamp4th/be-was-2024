package webserver;

import java.io.*;
import java.net.Socket;

import Mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import returnType.ContentTypeFactory;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final ContentTypeFactory contentTypeFactory = new ContentTypeFactory();

    private Socket connection;
    private ResponsePrinter responsePrinter;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String methodLine = br.readLine();

            String[] tokens = methodLine.split(" ");
            String method = tokens[0];
            String url = tokens[1];
            String mimeTypeForClient ="";

            logger.debug(methodLine);
            while(true){
                String line = br.readLine();
                if(line.isEmpty()) break;
                logger.debug(line);
                if(line.startsWith("Accept: ")){
                    tokens = line.split(" ");
                    mimeTypeForClient = tokens[1];
                }
            }
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest(method,url,mimeTypeForClient);
            responsePrinter = new ResponsePrinter(contentTypeFactory.getContentTypeFactory(httpRequest),new UserMapper());
            responsePrinter.makeResponse(httpRequest,out);

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
