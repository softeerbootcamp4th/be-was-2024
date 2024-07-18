package webserver.api.pagehandler;

import model.user.UserDAO;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.session.SessionDAO;
import webserver.util.HtmlFiles;

import java.io.IOException;


/**
 * 글쓰기 page를 반환하는 클래스
 */
public class WritePageHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new WritePageHandler();

        return single_instance;
    }

    /**
     * post 페이지를 반환한다
     * @param request 해당 요청에 대한 Httprequest class
     * @return 반환할 HttpResponse class
     */
    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(HtmlFiles.readHtmlByte(HtmlFiles.WRITE))
                .build();
    }
}
