package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.concurrent.*;

import handler.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * WebServer 클래스
 */
public class WebServer {
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;


    /**
     * Main 함수
     */
    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        ExecutorService threadPool = Executors.newCachedThreadPool();
        //톰캣(Tomcat 9 기준)의 default max pool size는 200
        //하지만 newCachedThreadPool()은 필요할 때 필요한 만큼의 쓰레드 풀을 생성함
        //이미 생성된 쓰레드가 있다면 이를 재활용 할 수 있음

        // 서버소켓을 생성한다. 웹서버는 기본적으로 8080번 포트를 사용한다.
        try (ServerSocket listenSocket = new ServerSocket(port)) {
            logger.info("Web Application Server started {} port.", port);

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null)
            {
                try
                {
                    threadPool.submit(new RequestHandler(connection));
                }
                catch(Exception a) {
                    logger.error(a.getMessage());
                }
            }
            threadPool.shutdown();

        }
        catch(IOException e)
        {
            logger.error("Server Start Error : "+ e.getMessage());
        }
    }
}
