package webserver;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Handler;
import utils.HttpRequestParser;
import utils.HttpResponseHandler;

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

            HttpRequestParser httpRequestParser = new HttpRequestParser(in);
            HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

            // 읽은 헤더 로깅
            String headers = httpRequestParser.headersToString();
            logger.debug(headers);

            // 요청 URL 파싱
            String url = httpRequestParser.getUrl();
            logger.info("요청 URL: " + url);

            Router router = new Router();
            Handler handler = router.getHandler(httpRequestParser);
            handler.handle(httpRequestParser, httpResponseHandler);

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
