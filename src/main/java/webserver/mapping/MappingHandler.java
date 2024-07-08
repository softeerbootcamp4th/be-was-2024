package webserver.mapping;

import webserver.mapping.mapper.GETCreateUserMapper;
import webserver.mapping.mapper.GETHomeMapper;
import webserver.mapping.mapper.GETRegistrationFormMapper;
import webserver.mapping.mapper.HttpMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MappingHandler {
    private static final Map<String, HttpMapper> getHandlers = new HashMap<>();
    private static final Map<String, HttpMapper> postHandlers = new HashMap<>();

    private static MappingHandler instance = new MappingHandler();

    private MappingHandler() {
    }

    public static MappingHandler getInstance() {
        return instance;
    }

    static {
        getHandlers.put("/", new GETHomeMapper());
        getHandlers.put("/registration", new GETRegistrationFormMapper());
        getHandlers.put("/user/create", new GETCreateUserMapper());

    }

    public Map<String, Object> mapping(String method, String path) throws IOException {
        int indexOfQuery = path.indexOf('?');

        String mappingPath = path;
        if (indexOfQuery != -1) {
            mappingPath = path.substring(0, indexOfQuery);
        }

        return switch (method) {
            case "GET" -> getHandlers.get(mappingPath).handle(path);
            case "POST" -> postHandlers.get(mappingPath).handle(path);
            default -> null;
        };
    }
}
