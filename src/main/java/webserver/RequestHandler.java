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

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequest httpRequest = HttpRequestParser.parse(in);
            logger.debug(httpRequest.toString());

            HttpResponse httpResponse = Logic.serve(httpRequest);

            response(out, httpResponse);

        } catch (InvalidHttpRequestException ie) {
            try {
                response(connection.getOutputStream(), HttpResponse.error(SC_BAD_REQUEST, ie.getMessage()));
            } catch (IOException e) {logger.error(e.getMessage());}
        } catch (UnsupportedHttpVersionException ue) {
            try {
                response(connection.getOutputStream(), HttpResponse.error(SC_HTTP_VERSION_NOT_SUPPORTED, ue.getMessage()));
            } catch (IOException e) {logger.error(e.getMessage());}
        }
        catch (Exception e) {
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
