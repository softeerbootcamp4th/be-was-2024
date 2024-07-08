package webserver.mapping.mapper;

import db.Database;
import model.User;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;
import java.util.Map;

@Deprecated
public class GETCreateUserMapper implements HttpMapper {
    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        Map<String, String> queries = httpRequest.getQuery();

        User newUser = new User(queries.get("userId"), queries.get("password"), queries.get("username"), queries.get("email"));
        Database.addUser(newUser);

        MyHttpResponse response = new MyHttpResponse(302, "Found", Map.of("Location", "/"), null);
        return response;
    }
}
