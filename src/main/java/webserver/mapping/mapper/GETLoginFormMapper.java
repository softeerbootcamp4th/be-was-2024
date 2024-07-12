package webserver.mapping.mapper;

import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.util.FileContentReader;

import java.io.IOException;

public class GETLoginFormMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        MyHttpResponse response = fileContentReader.readStaticResource(httpRequest.getPath() + "/index.html");
        return response;
    }
}
