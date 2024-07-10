package plugin;

import annotations.Get;
import annotations.Plugin;
import annotations.Post;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Session;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.Status;

import java.util.Map;

@Plugin
public class UserPlugin{

    public static final Logger logger = LoggerFactory.getLogger(UserPlugin.class);

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
        logger.debug(user.toString());

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

    @Get(path = "/login")
    public Response redirect(Request request) {
        return new Response.Builder(Status.SEE_OTHER)
                .addHeader("Location", "/login/index.html")
                .build();
    }

    @Post(path = "/login")
    public Response login(Request request){

        String body = new String(request.getBody());
        Map<String, String> map = Request.parseParameterMap(body);

        String userId = map.get("userId");
        String password = map.get("password");

        User user = Database.findUserById(userId);

        if(user==null) {
            logger.debug("user null");
            return new Response.Builder(Status.SEE_OTHER)
                    .addHeader("Location", "/user/login_failed.html")
                    .build();
        }

        if(user.getUserId().equals(userId) && user.getPassword().equals(password))
            return new Response.Builder(Status.SEE_OTHER)
                    .addHeader("Location", "/index.html")
                    .addHeader("Set-Cookie", "sid="+ Session.save(user)+"; Path=\\/")
                    .build();

        logger.debug(user.toString());

        return new Response.Builder(Status.SEE_OTHER)
                .addHeader("Location", "/user/login_failed.html")
                .build();

    }
}
