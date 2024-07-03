package webserver.mapping.mapper;

import db.Database;
import model.User;
import webserver.FileContentReader;
import webserver.HttpRequestParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetCreateUserMapper implements HttpMapper {
    private final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public Map<String, Object> handle(String path) throws IOException {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> queryMap = httpRequestParser.parseQuery(path);

        Database.addUser(User.toEntity(queryMap));

        response.put("code", 302);
        response.put("location", "/");


        return response;
    }
}
