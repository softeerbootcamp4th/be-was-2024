package webserver.mapping.mapper;

import webserver.FileContentReader;

import java.io.IOException;

public class GetHomeMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public byte[] handle(String path) throws IOException {
        String homePath = "/index.html";
        return fileContentReader.readStaticResource(homePath);
    }
}
