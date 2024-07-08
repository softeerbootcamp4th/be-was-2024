package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestObject;
import util.HttpResponseObject;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final FrontRequestProcess frontRequestProcess;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.frontRequestProcess = FrontRequestProcess.getInstance();
    }

    public void run() {
        //logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
          //      connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            // 요청 Header 출력
            String line = br.readLine(); // Request Line (ex: "GET /index.html HTTP/1.1")
            //logger.debug("request line: {}", line);
            HttpRequestObject httpRequestObject = HttpRequestObject.from(line);
            while(!line.isEmpty()) {
                //logger.debug("header: {}", line);
                line = br.readLine();
                httpRequestObject.putHeaders(line);
            }

            // Request Body
            StringBuilder body = new StringBuilder();
            while(br.ready()) {
                body.append((char) br.read());
            }
            if(!body.isEmpty()) {
                httpRequestObject.putBody(body.toString());
            }

            HttpResponseObject responseInfo = frontRequestProcess.handleRequest(httpRequestObject);
            frontRequestProcess.handleResponse(out, responseInfo);
        }  catch (IOException e) {
            logger.error("error: {}", e.getStackTrace());
        }
    }
}
