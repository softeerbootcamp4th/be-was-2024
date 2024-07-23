package webserver;
import dto.HttpRequest;
import dto.HttpResponse;
import exception.DatabaseException;
import exception.DynamicFileBuildException;
import exception.InvalidHttpRequestException;
import exception.ResourceNotFoundException;
import handler.Handler;
import handler.HandlerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.DatabaseConnection;
import util.ErrorResponseBuilder;
import util.HttpRequestParser;

import java.io.*;

/**
 * HttpRequest를 처리하는 클래스
 */
public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private final HandlerManager handlerManager = HandlerManager.getInstance();

    private Dispatcher() {}

    private static class LazyHolder {
        private static final Dispatcher INSTANCE = new Dispatcher();
    }

    public static Dispatcher getInstance() {
        return Dispatcher.LazyHolder.INSTANCE;
    }

    /**
     * HttpRequestParser를 통해 HttpRequest를 파싱한다.
     * 파싱 결과인 HttpRequest를 처리할 수 있는 Handler를 HandlerManager를 통해 가져온다.
     * Handler의 처리 결과를 담은 HttpResponse를 client에게 응답으로 보낸다.
     * 예외 발생 시, DB connection을 해제하고 예외응답을 client에게 보낸다.
     *
     * @param bis : HttpRequest를 읽을 수 있는 BufferedInputStream
     * @param dos : HttpResponse에 응답을 생성할 수 있는 DataOutputStream
     */
    public void dispatch(BufferedInputStream bis, DataOutputStream dos){
        try {
            // HttpRequest 파싱 및 결과 반환
            HttpRequest httpRequest = HttpRequestParser.parseHttpRequest(bis);

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
               | ResourceNotFoundException | DynamicFileBuildException | DatabaseException e){
            logger.error(e.getMessage());

            // DB connection 해제
            DatabaseConnection.closeConnection();

            HttpResponse httpResponse = new HttpResponse();
            ErrorResponseBuilder.buildErrorResponse(e, httpResponse);
        }
    }
}
