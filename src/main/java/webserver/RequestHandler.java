package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.HttpRequest;
import webserver.request.Path;
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
            String request = convert(in);
            logger.debug(request);

            HttpRequest httpRequest = new HttpRequest(request);

            if(httpRequest.getPath().isStatic()){
                ResponseHandler.response(200, out, httpRequest.getPath(), "");
            }else {
                if(httpRequest.getPath().get().equals("/registration")){
                    ResponseHandler.response(302, out, new Path("/registration/index.html"), "/registration/index.html");
                }
            }


        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String convert(InputStream inputStream) throws IOException {
        return getAllStrings(inputStream);
    }

}
