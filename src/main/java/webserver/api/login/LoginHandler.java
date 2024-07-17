package webserver.api.login;

import model.user.User;
import model.user.UserDAO;
import webserver.api.FunctionHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.enums.Extension;
import webserver.session.SessionDAO;
import webserver.util.HtmlFiles;
import webserver.http.response.PageBuilder;
import webserver.util.ParamsParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        UserDAO userDAO = new UserDAO();

        String id =params.get("id");
        String password = params.get("password");
        User user = userDAO.getUser(id);

        // if user information is not valid
        if(id == null || id.isEmpty()
                || password == null || password.isEmpty()
                || user == null || !Objects.equals(user.getPassword(), password)){
            return new HttpResponse.ResponseBuilder(401)
                    .addheader("Content-Type", Extension.HTML.getContentType())
                    .setBody(HtmlFiles.readHtmlByte(HtmlFiles.LOGIN_FAILED))
                    .build();
        }

        // if user information is valid
        SessionDAO sessionDAO = new SessionDAO();
        sessionDAO.deleteSessionByUserid(user.getUserId());
        String sessionString = sessionDAO.insertSession(user.getUserId());

        //go to logined main page
        return new HttpResponse.ResponseBuilder(200)
                .addheader("Content-Type", Extension.HTML.getContentType())
                .addheader("Set-Cookie","sid="+sessionString +"; Max-Age=3600; Path=/") //set cookie
                .setBody(PageBuilder.buildLoggedinPage(user.getName()))
                .build();
    }
}
