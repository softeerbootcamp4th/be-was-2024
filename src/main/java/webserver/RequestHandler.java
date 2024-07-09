package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder reqHeader = new StringBuilder();

            // Request Header 출력
            String reqLine = reader.readLine(), line;
            reqHeader.append("  ").append(reqLine).append("\n");
            while ((line = reader.readLine()) != null && !line.equals("")) {
                reqHeader.append("  ").append(line).append("\n");
            }
            logger.debug("\n:: Request ::\n{}", reqHeader);

            // Request 처리
            RequestDispatcher dispatcher = new RequestDispatcher(reqLine);
            RequestResult requestResult = dispatcher.dispatch();

            // Response 처리
            ResponseHandler responseHandler = new ResponseHandler(new DataOutputStream(out), requestResult);
            responseHandler.write();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
