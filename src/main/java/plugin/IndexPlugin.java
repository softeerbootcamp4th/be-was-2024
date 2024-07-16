package plugin;

import annotations.Get;
import annotations.Plugin;
import webserver.http.Session;
import webserver.http.request.Request;
import webserver.http.response.Response;
import webserver.http.response.Status;

import java.io.IOException;

import static util.Utils.getFile;
import static webserver.http.response.ResponseHandler.getContentType;

@Plugin
public class IndexPlugin {

    @Get(path = "/index.html")
    public Response index(Request request) throws IOException {

        String body = new String(getFile(request.getPath()));
        String replacedBody;

        if(request.isLogin()){
            replacedBody = body.replace("{USERNAME}", Session.get(request.getSessionId()).getName());
            replacedBody = replacedBody.replace("{LOGINBTN}", "<form action=\"/logout\" method=\"post\"><button type=\"submit\" class=\"btn btn_contained btn_size_s\">로그아웃</button></form>");
            replacedBody = replacedBody.replace("{WRITE}", "/article/index.html");
        }else {
            replacedBody = body.replace("{USERNAME}", "");
            replacedBody = replacedBody.replace("{LOGINBTN}", "<form action=\"/login\" method=\"get\"><button type=\"submit\" class=\"btn btn_contained btn_size_s\">로그인</button></form>");
            replacedBody = replacedBody.replace("{WRITE}", "/login");
        }
        return new Response.Builder(Status.OK)
                .addHeader("Content-Type", getContentType(request.getExtension()) + ";charset=utf-8")
                .body(replacedBody.getBytes())
                .build();

    }

}
