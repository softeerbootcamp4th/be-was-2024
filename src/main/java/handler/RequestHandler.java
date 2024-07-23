package handler;

import java.io.*;
import java.net.Socket;

import distributor.Distributor;
import model.ViewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import builder.BodyBuilder;
import webserver.Request;
import webserver.Response;

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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            while (!Thread.currentThread().isInterrupted()) {
                DataOutputStream dos = new DataOutputStream(out);

                Request request = Request.from(in);

                Distributor distributor = Distributor.from(request);
                distributor.process();

                ViewData viewData = distributor.getViewData();

                BodyBuilder bodyBuilder = BodyBuilder.from(viewData);
                byte[] body = bodyBuilder.getBody();

                Response response = new Response.Builder()
                        .url(viewData.getUrl())
                        .dataOutputStream(dos)
                        .redirectCode(viewData.getRedirectCode())
                        .statusCode(viewData.getStatusCode())
                        .cookie(viewData.getCookie())
                        .body(body)
                        .build();

                response.sendResponse();
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
