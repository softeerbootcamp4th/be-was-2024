package webserver;

import java.io.*;
import java.net.Socket;
import java.util.*;

import chain.core.ChainManager;
import http.MyHttpRequest;
import http.MyHttpResponse;
import http.utils.HttpResponseMessageBuildUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.core.IRouteHandler;
import url.MyURL;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final ChainManager chainManager;

    public RequestHandler(Socket connectionSocket, ChainManager chainManager )
    {
        this.connection = connectionSocket;
        this.chainManager = chainManager;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            /** request parsing phase **/
            // 맨 처음 요청 라인 읽기
            String requestLine = br.readLine();
            // header 목록 읽기
            List<String> headerLines = new ArrayList<>();
            String headerLine;
            while (!(headerLine = br.readLine()).isEmpty()) {
                headerLines.add(headerLine);
            }
            // body 부분은 나중에 추가적으로 확장

            /** handle & response phase **/

            MyHttpRequest request = new MyHttpRequest(requestLine, headerLines);
            MyHttpResponse response = new MyHttpResponse(request.getVersion());

            chainManager.execute(request, response);

            DataOutputStream dos = new DataOutputStream(out);

            byte[] responseMessage = HttpResponseMessageBuildUtil.build(response);
            logger.debug(new String(responseMessage));
            sendResponse(dos, responseMessage);
            logger.debug("send response");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, byte[] responseMessage) {
        try {
            dos.write(responseMessage, 0, responseMessage.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
