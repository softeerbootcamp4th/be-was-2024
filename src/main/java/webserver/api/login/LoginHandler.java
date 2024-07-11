package webserver.api.login;

import db.Database;
import model.User;
import webserver.api.FunctionHandler;
import webserver.http.HtmlFiles;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.Extension;
import webserver.http.response.PageBuilder;
import webserver.session.Session;
import webserver.util.ParamsParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Objects;

public class LoginHandler implements FunctionHandler {
    //singleton pattern
    private static FunctionHandler single_instance = null;
    public static synchronized FunctionHandler getInstance()
    {
        if (single_instance == null)
            single_instance = new LoginHandler();

        return single_instance;
    }

    @Override
    public HttpResponse function(HttpRequest request) throws IOException {
        String body = new String(request.getBody(), StandardCharsets.UTF_8);
        Map<String, String> params = ParamsParser.parseParams(body);

        String id =params.get("id");
        String password = params.get("password");
        User user = Database.findUserById(id);

        // if user information is not valid
        if(id == null || id.isEmpty()
                || password == null || password.isEmpty()
                || user == null || !Objects.equals(user.getPassword(), password)){

            byte[] responseBody = Files.readAllBytes(new File(HtmlFiles.login_failed).toPath());
            return new HttpResponse.ResponseBuilder(401)
                    .addheader("Content-Type", Extension.HTML.getContentType())
                    .addheader("Content-Length", String.valueOf(responseBody.length))
                    .setBody(responseBody)
                    .build();

        }

        // if user information is valid
        String sessionString = Session.createSession(user);
        byte[] responseBody = PageBuilder.buildLoggedinPage(user.getName());

        //go to logined main page
        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", Extension.HTML.getContentType())
                .addheader("Set-Cookie","sid="+sessionString +"; Path=/") //set cookie
                .setBody(responseBody)
                .build();
    }
}
