package webserver;

import java.io.*;
import java.net.Socket;
import http.HttpRequest;
import http.HttpResponse;
import logic.Logic;
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

            HttpRequest httpRequest = HttpRequest.read(in);
            logger.debug(httpRequest.toString());

            HttpResponse httpResponse = Logic.serve(httpRequest);

            response(out, httpResponse);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void response(OutputStream out, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes(httpResponse.headersToString());
        byte[] body = httpResponse.getBody();
        dos.write(body, 0, body.length);
        dos.flush();
    }

}
