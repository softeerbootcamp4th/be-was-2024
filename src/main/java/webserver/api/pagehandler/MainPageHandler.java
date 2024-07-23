package webserver.api.pagehandler;

import model.post.PostDAO;
import model.user.User;
import model.user.UserDAO;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.session.SessionDAO;
import webserver.util.HtmlFiles;
import webserver.http.response.PageBuilder;

import java.io.IOException;


/**
 * Main page에 대한 html response를 반환하는 클래스
 */
public class MainPageHandler implements FunctionHandler {
    // singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new MainPageHandler();
        return single_instance;
    }

    /**
     * main page response를 생성한다
     * <p>
     *     session id를 확인하고 loggedin page 또는 loggedout page를 넘겨준다
     * </p>
     * <p>
     *     이 과정에서 path variable을 찾아서 page builder에 넘겨준다
     * </p>
     * @see PageBuilder#buildLoggedinPage(String, String)
     * @see PageBuilder#buildLoggedoutPage(String)
     */
    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        String sessionid = request.getSessionid();
        SessionDAO sessionDAO = new SessionDAO();

        String postid = request.getPathVariables().get("postid");

        if(sessionid !=null && sessionDAO.findSession(sessionid) != null){
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUser(sessionDAO.findSession(sessionid));
            return new HttpResponse.ResponseBuilder(200)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildLoggedinPage(user.getName(),postid))
                    .build();
        }

        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(PageBuilder.buildLoggedoutPage(postid) )
                .build();
    }
}
