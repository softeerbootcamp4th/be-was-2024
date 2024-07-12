package webserver.mapping.mapper;

import webserver.FileContentReader;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;

public class GETRegistrationFormMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        MyHttpResponse response = fileContentReader.readStaticResource(httpRequest.getPath() + "/index.html");
        return response;
    }
}
