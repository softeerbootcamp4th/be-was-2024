package plugin;

import annotations.Get;
import annotations.Plugin;
import db.PostDatabase;
import model.Post;
import webserver.http.Session;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.Status;

import java.io.IOException;
import java.util.Map;

import static util.Utils.getFile;
import static webserver.http.response.ResponseHandler.getContentType;

@Plugin
public class IndexPlugin {

    @Get(path = "/index.html")
    public Response index(Request request) throws IOException {

        String body = new String(getFile(request.getPath()));
        String replacedBody;
        int postId = 0;

        Map<String, String> parameterMap = request.getParameter();
        if(parameterMap != null) {
            if(parameterMap.containsKey("postId")) {
                postId = Integer.parseInt(parameterMap.get("postId"));
            }
        }

        if(request.isLogin()){
            replacedBody = body.replace("{USERNAME}", Session.get(request.getSessionId()).getName());
            replacedBody = replacedBody.replace("{LOGINBTN}", "<form action=\"/logout\" method=\"post\"><button type=\"submit\" class=\"btn btn_contained btn_size_s\">로그아웃</button></form>");
            replacedBody = replacedBody.replace("{WRITE}", "/article/index.html");
        }else {
            replacedBody = body.replace("{USERNAME}", "");
            replacedBody = replacedBody.replace("{LOGINBTN}", "<form action=\"/login\" method=\"get\"><button type=\"submit\" class=\"btn btn_contained btn_size_s\">로그인</button></form>");
            replacedBody = replacedBody.replace("{WRITE}", "/login");
        }

        String title = "";
        String content = "";
        String authorName = "";

        if(PostDatabase.isExist(postId)){
            Post post = PostDatabase.findById(postId);
            title = post.getTitle();
            content = post.getContent();
            authorName = post.getAuthorName();
        }

        replacedBody = replacedBody.replace("{TITLE}", title);
        replacedBody = replacedBody.replace("{CONTENT}", content);
        replacedBody = replacedBody.replace("{AUTHOR_NAME}", authorName);

        return new Response.Builder(Status.OK)
                .addHeader("Content-Type", getContentType(request.getExtension()) + ";charset=utf-8")
                .body(replacedBody.getBytes())
                .build();

    }

}
