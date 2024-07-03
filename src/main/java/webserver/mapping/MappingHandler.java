package webserver.mapping;

import webserver.mapping.mapper.GetHomeMapper;
import webserver.mapping.mapper.GetRegistrationMapper;
import webserver.mapping.mapper.HttpMapper;

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
        getHandlers.put("/", new GetHomeMapper());
        getHandlers.put("/registration", new GetRegistrationMapper());
    }

    public byte[] mapping(String[] firstLine) {
        return switch (firstLine[0]) {
            case "GET" -> getHandlers.get(firstLine[1]).handle();
            case "POST" -> postHandlers.get(firstLine[1]).handle();
            default -> null;
        };
    }
}
