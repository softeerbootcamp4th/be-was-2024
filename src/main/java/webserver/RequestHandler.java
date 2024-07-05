package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.*;

import db.Database;
import model.User;

/**
 * Handles requests.
 */
public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final HttpRequestParser requestParser;
    private final UrlMapper urlMapper;
    private final StaticRequestHandler staticRequestHandler;
    private final DynamicRequestHandler dynamicRequestHandler;

    /**
     * Creates a new RequestHandler.
     *
     * @param connectionSocket the connection socket
     * @param requestParser the request parser
     * @param urlMapper the URL mapper
     * @param staticRequestHandler the static request handler
     * @param dynamicRequestHandler the dynamic request handler
     */
    public RequestHandler(Socket connectionSocket, HttpRequestParser requestParser,
                          UrlMapper urlMapper, StaticRequestHandler staticRequestHandler,
                          DynamicRequestHandler dynamicRequestHandler) {
        this.connection = connectionSocket;
        this.requestParser = requestParser;
        this.urlMapper = urlMapper;
        this.staticRequestHandler = staticRequestHandler;
        this.dynamicRequestHandler = dynamicRequestHandler;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}",
                connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest request = requestParser.parse(reader);
            HttpResponse response = new HttpResponse(dos);

            handleRequest(request, response);
        } catch (IOException e) {
            logger.error("Error handling request: {}", e.getMessage());
        }
    }

    /**
     * Handles the given request.
     *
     * @param request the request to handle
     * @param response the response to send
     */
    private void handleRequest(HttpRequest request, HttpResponse response) {
        String mappedUrl = urlMapper.mapUrl(request.getUrl());
        HttpRequest mappedRequest = request.withUrl(mappedUrl);

        if (isDynamicRequest(mappedRequest.getPath())) {
            dynamicRequestHandler.handle(mappedRequest, response);
        } else {
            staticRequestHandler.handle(mappedRequest, response);
        }
    }

    /**
     * Returns true if the request is dynamic.
     *
     * @param path the path of the request
     * @return true if the request is dynamic
     */
    private boolean isDynamicRequest(String path) {
        return path.startsWith("/create") || path.startsWith("/update") || path.startsWith("/delete");
    }
}