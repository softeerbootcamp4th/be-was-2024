package webserver.mapping.mapper;

import java.io.IOException;

public interface HttpMapper {
    byte[] handle(String path) throws IOException;
}
