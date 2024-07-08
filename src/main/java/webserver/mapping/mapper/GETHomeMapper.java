package webserver.mapping.mapper;

import webserver.FileContentReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GETHomeMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public Map<String, Object> handle(String path) throws IOException {
        Map<String, Object> response = new HashMap<>();

        response.put("code", 200);
        response.put("body", fileContentReader.readStaticResource("/index.html"));

        return response;
    }
}
