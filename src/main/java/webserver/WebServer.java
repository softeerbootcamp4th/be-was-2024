package webserver;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 간단한 웹 서버를 나타내는 클래스
 * 기본적으로 지정된 포트에서 클라이언트의 요청을 처리.
 */
public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

    /**
     * 웹 서버의 메인 메소드.
     * 지정된 포트에서 서버를 시작하고 클라이언트 요청을 처리.
     *
     * @param args 커맨드 라인 인수. 첫 번째 인수로 포트 번호를 받는다. 인수가 없는 경우 기본 포트인 8080을 사용합니다.
     */
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
            ExecutorService executor = Executors.newFixedThreadPool(10);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executor.execute(new RequestHandler(connection));
            }

            executor.shutdown();
        }
    }
}
