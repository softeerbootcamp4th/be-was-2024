package webserver.api.pagehandler;

import db.Database;
import model.User;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.response.PageBuilder;

import java.io.IOException;

public class UserListPageHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new UserListPageHandler();

        return single_instance;
    }


    private static final String userlistFormat1 =
            "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<meta charset=\"UTF-8\">"
            + "</head>"
            + "<body>"
            + "<ul>";

    private static final String getUserlistFormat2 =
            "</ul>"
            + "</body>"
            +"</html>";


    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(PageBuilder.buildUserList())
                .build();
    }
}
