package webserver.mapping.mapper.get;

import webserver.enums.HttpStatus;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.util.FileContentReader;

import java.io.IOException;

public class HomeMapper implements HttpMapper {
    private final FileContentReader fileContentReader = FileContentReader.getInstance();
    private final HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        MyHttpResponse response = new MyHttpResponse(HttpStatus.OK);
        if (httpRequestParser.isLogin(httpRequest)) {
            fileContentReader.readStaticResource("/main/index.html", response);
            return response;
        }
        fileContentReader.readStaticResource("/index.html", response);
        return response;
    }

}
