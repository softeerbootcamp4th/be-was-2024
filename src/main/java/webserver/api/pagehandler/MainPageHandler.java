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

public class MainPageHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new MainPageHandler();
        return single_instance;
    }

    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        String sessionid = request.getSessionid();
        SessionDAO sessionDAO = new SessionDAO();
        UserDAO userDAO = new UserDAO();

        if(sessionid !=null && sessionDAO.findSession(sessionid) != null){
            User user = userDAO.getUser(sessionDAO.findSession(sessionid));
            return new HttpResponse.ResponseBuilder(200)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildLoggedinPage(user.getName(),null))
                    .build();
        }

        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(HtmlFiles.readHtmlByte(HtmlFiles.MAIN_PAGE))
                .build();
    }
}
