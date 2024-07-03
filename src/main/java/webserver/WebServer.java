package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;
    private static final int THREAD_POOL_SIZE = 200; // 톰캣(Tomcat 9 기준)의 default max pool size는 200

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될 때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                threadPool.execute(new RequestHandler(connection));
            }
        } catch (IOException e) {
            logger.error("Server Start Error: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }
}
