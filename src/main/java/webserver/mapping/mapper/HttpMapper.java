package webserver.mapping.mapper;

import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;

public interface HttpMapper {
    MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException;
}
