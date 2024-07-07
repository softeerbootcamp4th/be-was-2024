package webserver.http.request;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.response.Response;
import webserver.http.response.ResponseHandler;

import static util.Utils.getAllStrings;

public class RequestHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            String request = getAllStrings(in);
            logger.debug(request);

            Request httpRequest = Request.parseRequest(request);
            Response response = ResponseHandler.response(httpRequest);

            out.write(response.toByte());

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
