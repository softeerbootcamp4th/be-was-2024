package webserver.mapping.mapper;

import java.io.IOException;
import java.util.Map;

public interface HttpMapper {
    Map<String, Object> handle(String path) throws IOException;
}
