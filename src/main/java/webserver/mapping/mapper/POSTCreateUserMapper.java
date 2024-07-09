package webserver.mapping.mapper;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class POSTCreateUserMapper implements HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(POSTCreateUserMapper.class);
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        Map<String, String> body = new HashMap<>();
        httpRequestParser.parseQuery(body, new String(httpRequest.getBody()));

        User newUser = new User(body.get("userId"), body.get("password"), body.get("name"), body.get("email"));
        Database.addUser(newUser);
        logger.debug("User created: {}", Database.findUserById(body.get("userId")));

        MyHttpResponse response = new MyHttpResponse(302, "Found", Map.of("Location", "/"), null);

        return response;
    }
}
