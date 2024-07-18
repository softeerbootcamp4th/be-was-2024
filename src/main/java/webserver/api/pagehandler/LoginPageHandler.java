package webserver.api.pagehandler;

import model.user.UserDAO;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.session.SessionDAO;
import webserver.util.HtmlFiles;

import java.io.IOException;

/**
 * login page를 반환하는 class
 */
public class LoginPageHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new LoginPageHandler();

        return single_instance;
    }

    /**
     * login page를 반환한다
     * <p>
     *     session을 확인하여 이미 로그인된 경우 main page로 redirect한다
     * </p>
     * @param request 해당 요청에 대한 Httprequest class
     * @return 반환할 HttpResponse class
     * @see SessionDAO
     */
    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        String sessionid = request.getSessionid();
        SessionDAO sessionDAO = new SessionDAO();

        if(sessionid !=null && sessionDAO.findSession(sessionid) != null){
            return new HttpResponse.ResponseBuilder(302)
                    .addheader("Location", "http://localhost:8080/")
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .build();
        }

        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(HtmlFiles.readHtmlByte(HtmlFiles.LOGIN))
                .build();
    }
}
