package webserver.mapping.mapper.post;

import db.ConnectionPool;
import db.SessionTable;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HttpStatus;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginUserMapper implements HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(LoginUserMapper.class);
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();
    ConnectionPool databaseConnections = ConnectionPool.getInstance();

    @Override
    public synchronized MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException, SQLException {
        Map<String, String> body = httpRequestParser.parseQuery(new String(httpRequest.getBody()));
        Connection connection = databaseConnections.getConnection();

        String userId = body.get("userId");
        User user = null;

        String sql = "SELECT * FROM Users WHERE userId = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, userId);
        logger.debug("SELECT * FROM Users WHERE userId = {}", userId);

        try (ResultSet rs = preparedStatement.executeQuery()) {
            if (rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
        }

        databaseConnections.releaseConnection(connection);

        // UserId not found or password does not match
        if (user == null || !user.getPassword().equals(body.get("password"))) {
            String redirectUrl = "/login?error=unauthorized";
            MyHttpResponse response = new MyHttpResponse(HttpStatus.FOUND, Map.of(
                    "Content-Type", "text/plain",
                    "Content-Length", "0",
                    "Location", redirectUrl
            ), new byte[0]);
            logger.debug("User not found: {}", userId);
            return response;
        }

        UUID sessionId = UUID.randomUUID();
        SessionTable.addSession(sessionId, user.getId());

        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Set-Cookie", "sId=" + sessionId + "; Path=/");
        responseHeaders.put("Location", "/");

        logger.debug("User logged in: {}", userId);

        MyHttpResponse response = new MyHttpResponse(HttpStatus.FOUND, responseHeaders);
        return response;
    }
}
