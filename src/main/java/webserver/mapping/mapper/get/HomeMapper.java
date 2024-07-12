package webserver.mapping.mapper.get;

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
        if (httpRequestParser.isLogin(httpRequest)) {
            return fileContentReader.readStaticResource("/main/index.html");
        }
        return fileContentReader.readStaticResource("/index.html");
    }

}
