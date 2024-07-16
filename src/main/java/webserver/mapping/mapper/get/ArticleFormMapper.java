package webserver.mapping.mapper.get;

import webserver.enums.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.util.FileContentReader;

import java.io.IOException;

public class ArticleFormMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        MyHttpResponse response = new MyHttpResponse(HttpStatus.OK);
        fileContentReader.readStaticResource(httpRequest.getPath() + "/index.html", response);
        return response;
    }
}
