package webserver;

import java.io.*;
import java.net.Socket;

import model.HttpRequest;
import model.HttpResponse;
import util.HttpSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestConverter;
import util.HttpResponseConverter;

/**
 * 클라이언트가 연결(http 요청)을
 */
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
            // inputStream -> HttpRequest 객체로 변환
            HttpRequestConverter httpRequestConverter = new HttpRequestConverter();
            HttpRequest httpRequest = httpRequestConverter.with(in);

            // HttpRequest -> HttpResponse 객체로 변환
            HttpResponseConverter httpResponseConverter = new HttpResponseConverter();
            HttpResponse httpResponse = httpResponseConverter.with(httpRequest);

            //HttpResponse 객체를 기반으로 outputStream으로 HttpResponse 발송
//            httpResponse.sendHttpResponse(out,httpResponse);
            HttpSender httpSender = new HttpSender();
            httpSender.sendHttpResponse(out,httpResponse);


        } catch (IOException e) {
            logger.error(e.getMessage());

        }
    }

}
