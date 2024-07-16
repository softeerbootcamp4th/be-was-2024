package webserver.api.pagehandler;

import model.user.User;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.util.HtmlFiles;
import webserver.http.response.PageBuilder;
import webserver.session.Session;

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

        if(sessionid !=null && Session.getSession(sessionid) != null){
            User user = Session.getSession(sessionid);
            return new HttpResponse.ResponseBuilder(200)
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .setBody(PageBuilder.buildLoggedinPage(user.getName()))
                    .build();
        }

        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(HtmlFiles.readHtmlByte(HtmlFiles.MAIN_PAGE))
                .build();
    }
}
