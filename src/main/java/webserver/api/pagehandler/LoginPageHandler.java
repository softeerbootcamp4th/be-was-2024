package webserver.api.pagehandler;

import webserver.api.FunctionHandler;
import webserver.http.response.HtmlFiles;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.response.PageBuilder;
import webserver.session.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LoginPageHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new LoginPageHandler();

        return single_instance;
    }


    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        String sessionid = request.getSessionid();

        if(sessionid !=null && Session.getSession(sessionid) != null){
            return new HttpResponse.ResponseBuilder(302)
                    .addheader("Location", "http://localhost:8080/")
                    .addheader("Content-Type", "text/html; charset=utf-8")
                    .build();
        }

        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(PageBuilder.buildLoginPage())
                .build();
    }
}
