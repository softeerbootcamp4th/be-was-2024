package webserver.mapping.mapper;

import db.Database;
import db.SessionTable;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class POSTLoginUserMapper implements HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(POSTLoginUserMapper.class);
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        Map<String, String> body = httpRequestParser.parseQuery(new String(httpRequest.getBody()));

        String userId = body.get("userId");
        User user = Database.findUserById(userId);

        // UserId not found
        if (user == null) {
            MyHttpResponse response = new MyHttpResponse(401, "Unauthorized", null, null);
            return response;
        }

        // Password mismatch
        if (!user.getPassword().equals(body.get("password"))) {
            MyHttpResponse response = new MyHttpResponse(401, "Unauthorized", null, null);
            return response;
        }

        UUID sessionId = UUID.randomUUID();
        SessionTable.addSession(sessionId, userId);

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Set-Cookie", "sId=" + sessionId + "; Path=/");
        responseHeaders.put("Location", "/");

        MyHttpResponse response = new MyHttpResponse(302, "Found", responseHeaders, null);

        return response;
    }
}
