package webserver;

import com.sun.net.httpserver.Request;
import constant.FileExtensionType;
import constant.HttpStatus;
import dto.HttpRequest;
import dto.HttpResponse;
import exception.DynamicFileBuildException;
import exception.InvalidHttpRequestException;
import exception.ResourceNotFoundException;
import handler.Handler;
import handler.HandlerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private final HandlerManager handlerManager = HandlerManager.getInstance();
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String ERROR_MESSAGE_404 =
            "<html>" +
                    "<head><title>404 Not Found</title></head>" +
                    "<body><h1>404 Not Found</h1></body>" +
            "</html>";

    private Dispatcher() {}

    private static class LazyHolder {
        private static final Dispatcher INSTANCE = new Dispatcher();
    }

    public static Dispatcher getInstance() {
        return Dispatcher.LazyHolder.INSTANCE;
    }

    public void dispatch(BufferedReader br, DataOutputStream dos) throws IOException {
        try {
            // HttpRequest 파싱 및 결과 반환
            HttpRequest httpRequest = HttpRequestParser.parseHttpRequest(br);

            // HandlerManager를 통해 request를 처리할 수 있는 handler 반환
            Handler handler = handlerManager.getHandler(httpRequest);

            // handler의 결과를 저장할 HttpResponse 객체 생성
            HttpResponse httpResponse = new HttpResponse();

            // handler 실행
            handler.handle(httpRequest, httpResponse);

            // HttpResponse를 client에게 응답
            httpResponse.sendHttpResponse(dos);
        }
        catch (IOException | IllegalArgumentException | InvalidHttpRequestException
               | ResourceNotFoundException | DynamicFileBuildException e){
            logger.error(e.getMessage());

            // 에러 응답을 저장할 HttpResponse 객체 생성
            HttpResponse httpResponse = new HttpResponse();
            httpResponse.setHttpStatus(HttpStatus.NOT_FOUND);
            httpResponse.addHeader(CONTENT_TYPE, FileExtensionType.HTML.getContentType());
            httpResponse.addHeader(CONTENT_LENGTH, String.valueOf(ERROR_MESSAGE_404.length()));
            httpResponse.setBody(ERROR_MESSAGE_404.getBytes());
            httpResponse.sendHttpResponse(dos);
        }
    }
}
