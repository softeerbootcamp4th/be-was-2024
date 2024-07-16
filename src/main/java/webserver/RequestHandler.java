package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequestParser;
import webserver.http.HttpResponseParser;
import webserver.http.MyHttpRequest;
import webserver.mapping.MappingHandler;
import webserver.util.FileContentReader;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final FileContentReader fileContentReader = FileContentReader.getInstance();
    private final MappingHandler mappingHandler = MappingHandler.getInstance();
    private final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    private final HttpResponseParser httpResponseParser = new HttpResponseParser(fileContentReader, mappingHandler);

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
//        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // Request
            MyHttpRequest httpRequest = httpRequestParser.parseRequest(in);
            logger.debug("httpRequest : {}", httpRequest);

            // Response
            DataOutputStream dos = new DataOutputStream(out);
            httpResponseParser.parseResponse(dos, httpRequest);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
