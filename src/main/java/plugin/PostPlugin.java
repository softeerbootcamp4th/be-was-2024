package plugin;

import annotations.Plugin;
import annotations.Post;
import db.PostDatabase;
import model.User;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.Status;

import java.util.Map;

@Plugin
public class PostPlugin {

    @Post(path = "/post")
    public Response post(Request request){

        Map<String, String> parameterMap = Request.parseParameterMap(new String(request.getBody()));

        User user = request.getUser().orElseThrow();

        model.Post post = new model.Post(
                parameterMap.get("content"),
                parameterMap.get("title"),
                user.getName()
        );

        PostDatabase.addPost(post);

        return new Response.Builder(Status.SEE_OTHER)
                .redirect("/index.html")
                .build();
    }

}
