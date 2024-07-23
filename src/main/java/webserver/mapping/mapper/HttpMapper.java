package webserver.mapping.mapper;

import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;
import java.sql.SQLException;

public interface HttpMapper {
    MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException, SQLException;
}
