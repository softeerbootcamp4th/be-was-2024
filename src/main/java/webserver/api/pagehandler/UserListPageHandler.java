package webserver.api.pagehandler;

import db.Database;
import model.User;
import webserver.api.FunctionHandler;
import webserver.http.HtmlFiles;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        StringBuilder builder = new StringBuilder();
        builder.append(userlistFormat1);

        for( User user :Database.findAll()){
            builder.append("<li>User : ").append(user.getName());
            builder.append("<ul>");
            builder.append("<li>ID : ").append(user.getUserId()).append("</li>");
            builder.append("<li>Password : ").append(user.getPassword()).append("</li>");
            builder.append("<li>Email : ").append(user.getEmail()).append("</li>");
            builder.append("</ul>");
            builder.append("</li>\r\n");
        }

        builder.append(getUserlistFormat2);

        byte[] body = builder.toString().getBytes("UTF-8");

        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", "text/html; charset=utf-8")
                .setBody(body)
                .build();


    }
}
