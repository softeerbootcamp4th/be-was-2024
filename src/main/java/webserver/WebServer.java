package webserver;

import java.net.ServerSocket;
import java.net.Socket;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private static final Database database = new Database(); // DB 초기화

    private static ExecutorService executor = Executors.newFixedThreadPool(10); // thread 갯수 제한을 위한 thread pool
    private static final Logger logger = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8080;

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

            // 클라이언트가 연결될때까지 대기한다.
            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                executor.submit(new RequestHandler(connection)); // 요청이 올 시에 executor queue에 작업 추가
            }
        }
    }

    public static void addUser(String id, String username, String password, String email) {
        database.addUser(new User(id, username, password, email));
        logger.info("user {} added.", id);
    }
}
