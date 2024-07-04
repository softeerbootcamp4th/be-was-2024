package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.response.ResponseHandler;

import static util.Utils.*;

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
            // TODO httpRequest에 toString을 적용해서 아래 로직을 없애기
            String request = getAllStrings(in);

            logger.debug(request);

            HttpRequest httpRequest = convert(in);
            ResponseHandler.response(out, request, httpRequest.getPath().getExtension());

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpRequest convert(InputStream inputStream) throws IOException {
        return new HttpRequest(getAllStrings(inputStream));
    }

}
