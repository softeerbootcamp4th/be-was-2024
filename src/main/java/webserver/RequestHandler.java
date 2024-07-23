package webserver;

import java.io.*;
import java.net.Socket;
import enums.HttpHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Handler;
import utils.HttpRequestParser;
import utils.HttpResponseHandler;
import utils.Model;
import view.View;
import view.ViewResolver;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String STATIC_RESOURCE_PATH = "src/main/resources/static";
    private Socket connection;
    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequestParser httpRequestParser = new HttpRequestParser(in);
            HttpResponseHandler httpResponseHandler = new HttpResponseHandler(out);

            // 읽은 헤더 로깅
            String headers = httpRequestParser.headersToString();
            logger.debug(headers);

            // 요청 URL 파싱
            String url = httpRequestParser.getUrl();
            logger.info("요청 URL: " + url);

            // 정적 리소스 요청 처리
            if (httpRequestParser.getExtension() != null) {
                StaticResourceHandler staticResourceHandler = new StaticResourceHandler(STATIC_RESOURCE_PATH);
                staticResourceHandler.handle(httpRequestParser, httpResponseHandler);
                return;
            }

            // 뷰 처리
            Model model = new Model();
            ViewResolver viewResolver = new ViewResolver(STATIC_RESOURCE_PATH, ".html");

            Router router = new Router();
            Handler handler = router.getHandler(httpRequestParser);
            String viewName = handler.handle(httpRequestParser, httpResponseHandler, model);

            if (viewName.startsWith("redirect:")) {
                String redirectUrl = viewName.substring("redirect:".length());
                httpResponseHandler
                        .addHeader(HttpHeader.LOCATION, redirectUrl)
                        .respond();
            } else {
                View view = viewResolver.resolve(viewName);
                String content = view.render(model.getAttributes());
                httpResponseHandler
                        .setBody(content.getBytes())
                        .addHeader(HttpHeader.CONTENT_TYPE, "text/html")
                        .respond();
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
