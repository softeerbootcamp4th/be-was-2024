package webserver.api;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.Extension;

import java.io.IOException;

/**
 * 권한 없는 접근 처리 클래스
 */
public class Unauthorized implements FunctionHandler {

    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new Unauthorized();
        return single_instance;
    }

    /**
     * 메인 페이지로 redirect 해준다
     * @param request 해당 요청에 대한 Httprequest class
     * @return 반환할 HttpResponse class
     */
    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        return new HttpResponse.ResponseBuilder(302)
                .addheader("Location", "http://localhost:8080/login")
                .addheader("Content-Type", Extension.HTML.getContentType())
                .build();
    }
}
