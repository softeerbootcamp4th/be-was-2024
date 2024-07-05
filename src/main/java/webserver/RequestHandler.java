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

        Response response = new Response();
        LogicProcessor logicProcessor = new LogicProcessor();

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            while (!Thread.currentThread().isInterrupted()) {
                // InputStream을 BufferedReader로 변환
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                DataOutputStream dos = new DataOutputStream(out);

                String requestLine = br.readLine();
                Request request = Request.from(requestLine);

                Distributor distributor = Distributor.from(request, response);
                distributor.process(dos);

//                if (request.getHttpMethod().equals("GET")) {
//                    if (request.isQueryString()) {
//                        logicProcessor.createUser(request);
//                        response.redirect("/index.html", dos, 302);
//                    } else {
//                        response.response(request.getPath(), dos);
//                    }
//                }

                // 읽어들인 InputStream 모두 출력
                String line;
                while (!(line = br.readLine()).isEmpty()) {
                    logger.debug(line); // 읽어들인 라인 출력
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
