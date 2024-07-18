package webserver.api.logout;

import model.user.User;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.Extension;
import webserver.session.SessionDAO;

import java.io.IOException;

/**
 * 로그아웃을 담당하는 class
 */
public class LogoutHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new LogoutHandler();

        return single_instance;
    }

    /**
     * 로그아웃을 진행하고 메인 페이지로 redirect한다
     * <p>
     *     이 과정에서 session cookie를 제거한다.
     * </p>
     * @param request 해당 요청에 대한 request
     * @return 반환할 response
     */
    @Override
    public HttpResponse function(HttpRequest request) throws IOException {

        //check session id
        String sessionid = request.getSessionid();
        if (sessionid != null){
            SessionDAO  sessionDAO = new SessionDAO();
            sessionDAO.deleteSession(sessionid);
        }

        //go to main page
        return new HttpResponse.ResponseBuilder(302)
                .addheader("Location", "http://localhost:8080/")
                .addheader("Set-Cookie", "sid=deleted ; Max-Age=0")
                .addheader("Content-Type", Extension.HTML.getContentType())
                .build();
    }
}
