package webserver.mapping.mapper;

import db.Database;
import webserver.FileContentReader;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;
import java.util.Map;

public class GETUserListMapper implements HttpMapper {
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();
    FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        StringBuilder userList = new StringBuilder();
        userList.append("<html><body><ul>");
        Database.findAll().forEach(user -> userList.append("<li>").append(user.getName()).append(" : ").append(user.getEmail()).append("</li>"));
        userList.append("</ul></body></html>");

        if (httpRequestParser.isLogin(httpRequest)) {
            return new MyHttpResponse(200, "OK", Map.of(), userList.toString().getBytes());
        }

        return new MyHttpResponse(302, "Found", Map.of("Location", "/login"), null);
    }
}
