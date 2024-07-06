package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import dto.HttpRequest;
import exception.HttpRequestParsingException;
import handler.Handler;
import handler.HandlerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestParser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final HandlerManager handlerManager;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        handlerManager = HandlerManager.getInstance();
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        InputStream in = null;
        OutputStream out = null;
        try {
            in = connection.getInputStream();
            out = connection.getOutputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);

            // HttpRequest 파싱 및 결과 반환
            HttpRequest parseResult = HttpRequestParser.parseHeader(br);

            // HandlerManager를 통해 request를 처리할 수 있는 handler 반환
            Handler handler = handlerManager.getHandler(parseResult);
            handler.handle(dos, parseResult.getQueryParams().orElse(new HashMap<>()));


        } catch (IOException | IllegalArgumentException | HttpRequestParsingException e) {
            logger.error(e.getMessage());

            // FileExtensionType에서 관리하지 않는 타입일 경우 404 error 응답
            response404Error(connection);
        }
        finally{
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                logger.error("Failed to close input stream", e);
            }
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                logger.error("Failed to close output stream", e);
            }
        }
    }

    private void response404Error(Socket connection) {
        try(OutputStream out = connection.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out)){
            handlerManager.response404Error(dos);
        }
        catch (IOException e){
            logger.error(e.getMessage());
        }
    }
}
