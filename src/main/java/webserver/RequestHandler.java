package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.mapper.MappingHandler;

/**
 * 들어온 request에 대한 정보를 처리하는 메서드
 */
public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
//        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            HttpRequestParser requestParser = new HttpRequestParser();
            HttpRequest httpRequest = requestParser.parse(in);

//            logger.debug("request line : {} {}", httpRequest.getMethod(), httpRequest.getUrl());
//

            for (Map.Entry<String, String> entry : httpRequest.getHeaders().entrySet()) {
                logger.debug("header : {}={}", entry.getKey(), entry.getValue());
            }


            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = new HttpResponse(httpRequest, dos);
            MappingHandler.mapRequest(httpRequest, httpResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
