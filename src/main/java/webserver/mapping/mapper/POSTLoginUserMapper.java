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

        // UserId not found or password does not match
        if (user == null || !user.getPassword().equals(body.get("password"))) {
            String redirectUrl = "/login?error=unauthorized";
            MyHttpResponse response = new MyHttpResponse(302, "Found", Map.of(
                    "Content-Type", "text/plain",
                    "Content-Length", "0",
                    "Location", redirectUrl
            ), new byte[0]);
            logger.debug("User not found: {}", userId);
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
