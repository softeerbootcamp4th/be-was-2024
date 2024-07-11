package webserver.api.pagehandler;

import model.User;
import webserver.api.FunctionHandler;
import webserver.api.registration.Registration;
import webserver.http.HtmlFiles;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.response.PageBuilder;
import webserver.session.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
                .setBody(Files.readAllBytes(new File(HtmlFiles.main_page).toPath()))
                .build();
    }
}
