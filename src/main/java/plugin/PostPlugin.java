package plugin;

import annotations.Plugin;
import annotations.Post;
import db.PostH2Database;
import model.User;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.Status;

import java.sql.SQLException;
import java.util.Map;

@Plugin
public class PostPlugin {

    @Post(path = "/post")
    public Response post(Request request) throws SQLException {

        Map<String, String> parameterMap = Request.parseParameterMap(new String(request.getBody()));

        User user = request.getUser().orElseThrow();

        model.Post post = new model.Post(
                parameterMap.get("content"),
                parameterMap.get("title"),
                user.getName()
        );

        PostH2Database.addPost(post);

        return new Response.Builder(Status.SEE_OTHER)
                .redirect("/index.html")
                .build();
    }

}
