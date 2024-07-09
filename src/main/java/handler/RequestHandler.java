package handler;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestObject;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private RequestObject requestObject;
    private final GetHandler getHandler;
    private final PostHandler postHandler;

    public RequestHandler(Socket connectionSocket)
    {
        this.connection = connectionSocket;
        this.getHandler =GetHandler.getInstance();
        this.postHandler=PostHandler.getInstance();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            requestObject = new RequestObject(in);
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            requestDistribute(dos,requestObject);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    //각 요청을 메소드에 맞게 뿌려준다
    private void requestDistribute(DataOutputStream dos,RequestObject requestObject)
    {
        String method = requestObject.getMethod();
        if(method.equals("GET") )//GET방식 들어올 경우
        {
            getHandler.handleGetRequest(dos,requestObject);
        }
        else if(method.equals("POST"))
        {
            postHandler.handlePostRequest(dos,requestObject);
        }
    }

}
