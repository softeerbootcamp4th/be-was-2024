package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.DynamicRequestHandler;
import util.HttpRequestParser;
import util.StaticRequestHandler;
import util.UrlMapper;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = Config.DEFAULT_PORT;
    private static final int THREAD_POOL_SIZE = Config.THREAD_POOL_SIZE;
    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
            // 클라이언트가 연결될때까지 대기한다.
//            Socket connection;
//            while ((connection = listenSocket.accept()) != null) {
//                Thread thread = new Thread(new RequestHandler(connection));
//                thread.start();
//            }
            HttpRequestParser requestParser = new HttpRequestParser();
            UrlMapper urlMapper = new UrlMapper();
            StaticRequestHandler staticRequestHandler = new StaticRequestHandler();
            DynamicRequestHandler dynamicRequestHandler = new DynamicRequestHandler();
            while (true) {
                Socket connection = listenSocket.accept();
                executor.execute(new RequestHandler(connection, requestParser, urlMapper,
                        staticRequestHandler, dynamicRequestHandler));
            }
        }
    }
}
