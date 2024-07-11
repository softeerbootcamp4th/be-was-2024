package webserver.api.logout;

import webserver.api.FunctionHandler;
import webserver.api.login.LoginHandler;
import webserver.http.HtmlFiles;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.Extension;
import webserver.session.Session;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LogoutHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new LogoutHandler();

        return single_instance;
    }

    @Override
    public HttpResponse function(HttpRequest request) throws IOException {

        //check session id
        String sessionid = request.getSessionid();
        if (sessionid != null){
            Session.deleteSession(sessionid);
        }

        //go to main page
        return new HttpResponse.ResponseBuilder(302)
                .addheader("Location", "http://localhost:8080/")
                .addheader("Set-Cookie", "sid=deleted ; Max-Age=0")
                .addheader("Content-Type", "text/html; charset=utf-8")
                .build();
    }
}
