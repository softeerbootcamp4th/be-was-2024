package webserver.mapping.mapper;

import db.SessionTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class POSTLogoutUserMapper implements HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(POSTLogoutUserMapper.class);
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {

        SessionTable.removeSession(UUID.fromString(httpRequestParser.parseCookie(httpRequest.getHeaders().get("Cookie")).get("sId")));

        MyHttpResponse response = new MyHttpResponse(HttpStatus.FOUND, Map.of(
                "Content-Type", "text/plain",
                "Content-Length", "0",
                "Set-Cookie", "sId=; Path=/; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT",
                "Location", "/"
        ), new byte[0]);

        return response;
    }
}
