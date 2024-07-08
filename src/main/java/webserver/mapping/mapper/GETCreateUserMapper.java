package webserver.mapping.mapper;

import db.Database;
import model.User;
import webserver.FileContentReader;
import webserver.HttpRequestParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GETCreateUserMapper implements HttpMapper {
    private final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public Map<String, Object> handle(String path) throws IOException {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> queryMap = httpRequestParser.parseQuery(path);

        User newUser = new User(queryMap.get("userId"), queryMap.get("password"), queryMap.get("username"), queryMap.get("email"));
        Database.addUser(newUser);

        response.put("code", 302);
        response.put("location", "/");


        return response;
    }
}
