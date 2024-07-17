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

/**
 * 게시글과 관련된 처리를 하는 클래스
 */
@Plugin
public class PostPlugin {

    /**
     * post 로 요청이 왔을 때 게시글 업로드를 처리하는 메소드
     * @param request
     * @return
     * @throws SQLException
     */
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
