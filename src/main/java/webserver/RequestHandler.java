package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequest;
import util.HttpResponse;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final FrontRequestProcess frontRequestProcess;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.frontRequestProcess = FrontRequestProcess.getInstance();
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line = br.readLine(); // Request Line (ex: "GET /index.html HTTP/1.1")
            HttpRequest httpRequest = HttpRequest.from(line);

            // Request Headers
            while(!line.isEmpty()) {
                line = br.readLine();
                httpRequest.putHeaders(line);
            }

            // Request Body
            List<Byte> body = new ArrayList<>();
            while(br.ready()) {
                body.add((byte) br.read());
            }
            if(!body.isEmpty()) {
                httpRequest.putBody(body);
            }

            HttpResponse responseInfo = frontRequestProcess.handleRequest(httpRequest);
            frontRequestProcess.handleResponse(out, responseInfo);
        }  catch (IOException e) {
            logger.error("error: {}", e.getStackTrace());
        }
    }
}
