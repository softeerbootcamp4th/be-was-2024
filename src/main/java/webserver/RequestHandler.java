package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ConstantUtil;
import util.HttpRequest;
import util.HttpResponse;
import util.StringParser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private final FrontRequestProcess frontRequestProcess;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.frontRequestProcess = FrontRequestProcess.getInstance();
    }

    public void run() {
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            String requestLine = StringParser.readLine(in); // Request Line (ex: "GET /index.html HTTP/1.1")
            HttpRequest httpRequest = HttpRequest.from(requestLine);
            int contentLength = 0;

            // Request Headers
            String line;
            while (!(line = StringParser.readLine(in)).isEmpty()) {
                httpRequest.putHeaders(line);
            }
            if(httpRequest.getContentLength() != 0) {
                contentLength = httpRequest.getContentLength();
            }

            // Request Body
            byte[] body = in.readNBytes(contentLength);
            httpRequest.putBody(body);

            // Multipart Data Handling
            if(httpRequest.getContentType().contains(ConstantUtil.FORM_DATA)) {
                String boundary = httpRequest.getContentType().split(ConstantUtil.BOUNDARY_WITH_EQUAL)[1];
                StringParser.processMultipartData(httpRequest, boundary);
            }

            HttpResponse responseInfo = frontRequestProcess.handleRequest(httpRequest);
            frontRequestProcess.handleResponse(out, responseInfo);
        }  catch (IOException e) {
            logger.error("error: {}", e.getStackTrace());
        }
    }
}
