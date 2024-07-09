package webserver;

import java.io.*;
import java.net.Socket;
import java.util.*;

import http.MyHttpRequest;
import http.MyHttpResponse;
import http.utils.HttpResponseMessageBuildUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import routehandler.IRouteHandler;
import routehandler.RouteHandlerMatcher;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final RouteHandlerMatcher matcher;

    public RequestHandler(Socket connectionSocket, RouteHandlerMatcher matcher )
    {
        this.connection = connectionSocket;
        this.matcher = matcher;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            String requestLine = br.readLine();

            List<String> headerLines = new ArrayList<>();
            String buffer;
            while (!(buffer = br.readLine()).isEmpty()) {
                headerLines.add(buffer);
            }
            // body 부분은 나중에 추가적으로 확장
            MyHttpRequest request = new MyHttpRequest(requestLine, headerLines);
            MyHttpResponse response = new MyHttpResponse(request.getVersion());

            String url = request.getUrl();
            IRouteHandler routeHandler = matcher.getMatchedRouteHandler(url);

            routeHandler.handle(request, response);
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
