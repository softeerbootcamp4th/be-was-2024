package plugin;

import annotations.Get;
import annotations.Plugin;
import annotations.Post;
import db.Database;
import model.User;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.Status;

import java.util.Map;

@Plugin
public class UserPlugin{

    @Post(path = "/create")
    public Response create(Request request) {
        byte[] body = request.getBody();
        Map<String, String> parameterMap = Request.parseParameterMap(new String(body));
        User user = new User(
                parameterMap.get("userId"),
                parameterMap.get("password"),
                parameterMap.get("name"),
                parameterMap.get("email")
        );
        Database.addUser(user);

        return new Response.Builder(Status.SEE_OTHER)
                .addHeader("Location", "/index.html")
                .build();

    }

    @Get(path = "/registration")
    public Response registration(Request request){
        return new Response.Builder(Status.SEE_OTHER)
                .addHeader("Location", "/registration/index.html")
                .build();
    }

}
