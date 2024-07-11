package webserver.mapping.mapper;

import db.Database;
import webserver.annotation.LoginCheck;
import webserver.annotation.processor.LoginCheckProcessor;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;
import java.util.Map;

@LoginCheck
public class GETUserListMapper implements HttpMapper {
    LoginCheckProcessor loginCheckProcessor = new LoginCheckProcessor();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        try {
            return getUserList();
        } catch (Exception e) {
            return new MyHttpResponse(500, "Internal Server Error", Map.of(), "Internal Server Error".getBytes());
        }
    }

    public MyHttpResponse getUserList() {
        StringBuilder userList = new StringBuilder();
        userList.append("<html><body><ul>");
        Database.findAll().forEach(user -> userList.append("<li>").append(user.getName()).append(" : ").append(user.getEmail()).append("</li>"));
        userList.append("</ul></body></html>");

        return new MyHttpResponse(200, "OK", Map.of(), userList.toString().getBytes());
    }
}
