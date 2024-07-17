package plugin;

import annotations.Get;
import annotations.Plugin;
import annotations.Post;
import db.UserDatabase;
import db.UserH2Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Session;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.Status;

import java.util.Map;

/**
 * 회원과 관련된 처리를 하는 클래스
 */
@Plugin
public class UserPlugin{

    public static final Logger logger = LoggerFactory.getLogger(UserPlugin.class);

    /**
     * /create 로 요청이 들어왔을 때 회원 생성을 처리하는 메소드
     * @param request
     * @return
     */
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
        logger.debug(user.toString());
        UserH2Database.addUser(user);
        //logger.debug(user.toString());

        return new Response.Builder(Status.SEE_OTHER)
                .redirect("/index.html")
                .build();

    }

    /**
     * /registration 으로 요청이 들어왔을 때 적절한 html 리소스로 리다이렉션하는 메소드
     * @param request
     * @return
     */
    @Get(path = "/registration")
    public Response registration(Request request){
        return new Response.Builder(Status.SEE_OTHER)
                .redirect("/registration/index.html")
                .build();
    }

    /**
     * /login 으로 요청이 들어왔을 때 적절한 html 리소스로 리다이렉션하는 메소드
     * @param request
     * @return
     */
    @Get(path = "/login")
    public Response redirect(Request request) {
        return new Response.Builder(Status.SEE_OTHER)
                .redirect("/login/index.html")
                .build();
    }

    /**
     * /login 으로 요청이 들어왔을 때 회원을 인증하는 메소드
     * @param request
     * @return
     */
    @Post(path = "/login")
    public Response login(Request request){

        String body = new String(request.getBody());
        Map<String, String> map = Request.parseParameterMap(body);

        String userId = map.get("userId");
        String password = map.get("password");

        User user = UserH2Database.findUserById(userId).orElse(null);

        if(user==null) {
            logger.debug("user null");
            return new Response.Builder(Status.SEE_OTHER)
                    .redirect("/user/login_failed.html")
                    .build();
        }

        if(user.getUserId().equals(userId) && user.getPassword().equals(password))
            return new Response.Builder(Status.SEE_OTHER)
                    .redirect("/index.html")
                    .addHeader("Set-Cookie", "sid="+ Session.save(user)+"; Path=\\/")
                    .build();

        logger.debug(user.toString());

        return new Response.Builder(Status.SEE_OTHER)
                .redirect("/user/login_failed.html")
                .build();

    }

    /**
     * 로그아웃을 처리하는 메소드
     * @param request
     * @return
     */
    @Post(path = "/logout")
    public Response logout(Request request){

        logger.debug("logout");
        String sessionId = request.getSessionId();
        Session.delete(sessionId);

        return new Response.Builder(Status.SEE_OTHER)
                .redirect("/index.html")
                .build();

    }

    /**
     * 유저 리스트를 출력하는 메소드
     * @param request
     * @return
     */
    @Get(path = "/user/list")
    public Response userList(Request request){

        if(!request.isLogin()) return new Response.Builder(Status.SEE_OTHER)
                .redirect("/login/index.html")
                .build();

        StringBuilder sb = new StringBuilder();

        for(User user: UserH2Database.findAll()){
            sb.append(user.toString()).append("\n");
        }

        return new Response.Builder(Status.OK)
                .body(sb.toString())
                .build();
    }

}
