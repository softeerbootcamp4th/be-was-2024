package webserver.mapping;

import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
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

    public MyHttpResponse mapping(MyHttpRequest httpRequest) throws IOException {
        String method = httpRequest.getMethod();
        String path = httpRequest.getPath();

        return switch (method) {
            case "GET" -> getHandlers.get(path).handle(httpRequest);
            case "POST" -> postHandlers.get(path).handle(httpRequest);
            default -> null;
        };
    }
}
