package handler;

import java.io.*;
import java.net.Socket;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processor.UserProcessor;
import util.FileDetection;
import util.RequestObject;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    private RequestObject requestObject ;
    private final GetHandler getHandler;
    private final PostHandler postHandler;
    private final UserProcessor userProcessor;


    public RequestHandler(Socket connectionSocket)
    {
        this.connection = connectionSocket;
        this.getHandler =GetHandler.getInstance();
        this.postHandler=PostHandler.getInstance();
        this.userProcessor=UserProcessor.getInstance();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String line = br.readLine();
            logger.debug("HTTP request first line : {} ",line);// 가장 첫번째 줄, 즉 request line
            requestObject = new RequestObject(line);
            StringBuilder sb = new StringBuilder();//각 쓰레드가 막 출력하던걸 한번에 담아서 헷갈리지 않게
            while(!line.equals(""))
            {
                line = br.readLine();
                sb.append(line).append("\n");
            }
            logger.debug(sb.toString());
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            requestDistribute(dos,requestObject);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void requestDistribute(DataOutputStream dos,RequestObject requestObject)
    {
        String method = requestObject.getMethod();
        String path = requestObject.getPath();
        if(method.equals("GET") )//GET방식 들어올 경우
        {
            path = FileDetection.getPath(FileDetection.fixedPath+path);
            getHandler.staticFileHandler(dos,path);
        }
        else if(method.equals("POST"))
        {
            userProcessor.userCreate(requestObject);
            postHandler.responseAlert(dos,"로그인 성공");
        }
    }

}
