package webserver;

import java.io.*;
import java.net.Socket;

import exception.InvalidHttpRequestException;
import exception.UnsupportedHttpVersionException;
import http.HttpRequest;
import http.HttpRequestParser;
import http.HttpResponse;
import logic.Logic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static util.HttpStatus.*;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static final Logic logic = new Logic();

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest;
            try{
                httpRequest = HttpRequestParser.parse(in);
            } catch (InvalidHttpRequestException ie) {
                logger.debug(ie.getMessage());
                response(out, HttpResponse.error(SC_BAD_REQUEST, ie.getMessage()));
                return;
            } catch (UnsupportedHttpVersionException ue) {
                logger.debug(ue.getMessage());
                response(out, HttpResponse.error(SC_HTTP_VERSION_NOT_SUPPORTED, ue.getMessage()));
                return;
            }

            logger.debug(httpRequest.toString());

            HttpResponse httpResponse = logic.serve(httpRequest);

            response(out, httpResponse);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

    }

    private void response(OutputStream out, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeBytes(httpResponse.headersToString());
        byte[] body = httpResponse.getBody();
        if(body != null) {
            dos.write(body, 0, body.length);
            dos.flush();
        }
    }

}
