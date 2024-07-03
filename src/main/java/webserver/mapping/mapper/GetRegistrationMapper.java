package webserver.mapping.mapper;

import webserver.FileContentReader;

import java.io.IOException;

public class GetRegistrationMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public byte[] handle(String path) throws IOException {
        String filePath = "/registration/index.html";
        return fileContentReader.readStaticResource(filePath);
    }
}
