package webserver.mapping.mapper.error;

import webserver.enums.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.util.FileContentReader;

import java.io.IOException;

public class MethodNotAllowedMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        MyHttpResponse response = new MyHttpResponse(HttpStatus.METHOD_NOT_ALLOWED);
        fileContentReader.readStaticResource("/405.html", response);
        return response;
    }
}
