package webserver.mapper;

import webserver.HttpRequest;
import webserver.HttpResponse;
import webserver.enumPackage.HttpStatus;

import java.io.IOException;

/**
 * 들어온 요청을 메서드 별로 분기시켜주는 클래스
 */
public class MappingHandler {

    /**
     * 들어온 요청의 메서드 별로 분기를 담당하는 메서드
     * @param httpRequest 요청된 정보를 담은 객체
     * @param httpResponse 응답 보낼 정보를 담은 객체
     * @throws IOException
     */
    public static void mapRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        String method = httpRequest.getMethod();

        switch (method) {
            case "GET":
                GetHandler.handle(httpRequest, httpResponse);
                break;
            case "POST":
                PostHandler.handle(httpRequest, httpResponse);
                break;
            default:
                httpResponse.sendErrorPage(HttpStatus.METHOD_NOT_ALLOWED.getMessage(), "/");
                break;
        }
    }

}
