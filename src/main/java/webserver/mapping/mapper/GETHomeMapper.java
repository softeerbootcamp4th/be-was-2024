package webserver.mapping.mapper;

import webserver.FileContentReader;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;

public class GETHomeMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();
    
    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        MyHttpResponse response = fileContentReader.readStaticResource("/index.html");
        return response;
    }
}
