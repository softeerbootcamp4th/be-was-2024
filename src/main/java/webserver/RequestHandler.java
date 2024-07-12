package webserver;

import common.RequestUtils;
import common.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private RequestHandler() {
    }

    private static class InnerInstanceClass {
        private static final RequestHandler instance = new RequestHandler();
    }

    public static RequestHandler getInstance() {
        return InnerInstanceClass.instance;
    }

    public void setConnection(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            // 요청 헤더를 파싱하여 Request객체 생성
            HttpRequest httpRequest = RequestUtils.parseHttpRequest(in);
            // Request객체의 정보를 바탕으로 적절한 응답하기
            handleRequest(httpRequest, out);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    /**
     * REST 요청과 일반 요청을 분기
     */
    public void handleRequest(HttpRequest request, OutputStream out) throws IOException {
        String filePath = request.getPath();

        if(WebUtils.isRESTRequest(filePath)) {
            WebAdapter.resolveRequest(request, out);
        } else {
            ViewResolver.responseProperFileFromRequest(request, out);
        }
    }
}
