package webserver.http;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.request.Request;
import webserver.http.request.RequestReader;
import webserver.http.response.Response;
import webserver.http.response.ResponseHandler;
import webserver.http.response.ResponseWriter;

/**
 * 요청을 응답으로 변환하고 출력하는 클래스
 */
public class HttpHandler implements Runnable {
    public static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    private Socket connection;
    private final ResponseHandler responseHandler;
    private final RequestReader requestReader;
    private final ResponseWriter responseWriter;

    public HttpHandler(Socket connectionSocket, ResponseHandler responseHandler, RequestReader requestReader, ResponseWriter responseWriter) {
        this.connection = connectionSocket;
        this.responseHandler = responseHandler;
        this.requestReader = requestReader;
        this.responseWriter = responseWriter;
    }

    /**
     * 요청을 응답으로 변환하고 출력하는 메소드
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try {
            Request request = requestReader.readRequest();
            Response response = responseHandler.response(request);
            responseWriter.write(response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
