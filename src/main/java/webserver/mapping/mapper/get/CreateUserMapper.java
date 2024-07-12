package webserver.mapping.mapper.get;

import db.Database;
import model.User;
import webserver.enums.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;

import java.io.IOException;
import java.util.Map;

@Deprecated
public class CreateUserMapper implements HttpMapper {
    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        Map<String, String> queries = httpRequest.getQuery();

        User newUser = new User(queries.get("userId"), queries.get("password"), queries.get("username"), queries.get("email"));
        Database.addUser(newUser);

        MyHttpResponse response = new MyHttpResponse(HttpStatus.FOUND, Map.of("Location", "/"));
        return response;
    }
}
