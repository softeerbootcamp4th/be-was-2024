package webserver.mapping.mapper.post;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;
import webserver.exception.InvalidSignUpParameterException;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;

import java.io.IOException;
import java.util.Map;

public class CreateUserMapper implements HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserMapper.class);
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        Map<String, String> body = httpRequestParser.parseQuery(new String(httpRequest.getBody()));

        String userId = body.get("userId");
        String password = body.get("password");
        String name = body.get("name");
        String email = body.get("email");

        if (userId == null || password == null || name == null || email == null) {
            throw new InvalidSignUpParameterException("Invalid sign up parameter");
        }

        User newUser = new User(userId, password, name, email);
        Database.addUser(newUser);
        logger.debug("User created: {}", Database.findUserById(body.get("userId")));

        MyHttpResponse response = new MyHttpResponse(HttpStatus.FOUND, Map.of("Location", "/"), null);

        return response;
    }
}
