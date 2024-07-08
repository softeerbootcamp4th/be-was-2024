package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import chain.NotFoundHandleChain;
import chain.RouteHandleChain;
import chain.StaticResourceChain;
import chain.core.ChainManager;
import chain.core.MiddlewareChain;
import config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.route.IndexRouteHandler;
import routehandler.route.RegistrationRouteHandler;
import routehandler.utils.Route;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = AppConfig.DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }
        MiddlewareChain chain = new RouteHandleChain(
                        Route.at("/registration")
                                .GET(new RegistrationRouteHandler())
                                .routes(
                                        Route.at("{test}")
                                                .GET(new IndexRouteHandler())
                                ),
                        Route.at("/")
                                .GET(new IndexRouteHandler())
                                .POST(new IndexRouteHandler())
                );

        ChainManager chainManager = new ChainManager(
                new StaticResourceChain(),
                chain,
                new NotFoundHandleChain()
        );

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(AppConfig.THREAD_POOL_SIZE);

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            // new socket이 실행되므로, 각 요청마다 다른 처리가 가능.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executor.submit(new RequestHandler(connection, chainManager));
            }
        }
    }
}
