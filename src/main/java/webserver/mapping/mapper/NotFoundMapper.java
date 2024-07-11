package webserver.mapping.mapper;

import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.util.Map;

public class NotFoundMapper implements HttpMapper {
    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) {
        return new MyHttpResponse(404, "Not Found", Map.of(), "Not Found".getBytes());
    }
}
