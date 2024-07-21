package webserver.mapping.mapper.post;

import db.ConnectionPool;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.enums.HtmlFlag;
import webserver.enums.HttpStatus;
import webserver.exception.InvalidSignUpParameterException;
import webserver.http.HttpRequestParser;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.util.FileContentReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;

public class CreateUserMapper implements HttpMapper {
    private static final Logger logger = LoggerFactory.getLogger(CreateUserMapper.class);
    HttpRequestParser httpRequestParser = HttpRequestParser.getInstance();
    FileContentReader fileContentReader = FileContentReader.getInstance();
    ConnectionPool databaseConnections = ConnectionPool.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        Map<String, String> body = httpRequestParser.parseQuery(new String(httpRequest.getBody()));
        Connection connection = databaseConnections.getConnection();

        String userId = body.get("userId");
        String password = body.get("password");
        String name = body.get("name");
        String email = body.get("email");

        if (userId == null || password == null || name == null || email == null) {
            throw new InvalidSignUpParameterException("Invalid sign up parameter : userId, password, name, email required");
        }

        try {

            User newUser = new User(userId, password, name, email);
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, newUser.getUserId());
            preparedStatement.setString(2, newUser.getPassword());
            preparedStatement.setString(3, newUser.getName());
            preparedStatement.setString(4, newUser.getEmail());
            preparedStatement.executeUpdate();

            logger.debug("INSERT INTO users (userId, password, name, email) VALUES ({}, {}, {}, {})", userId, password, name, email);

            preparedStatement.close();
            databaseConnections.releaseConnection(connection);

            return new MyHttpResponse(HttpStatus.FOUND, Map.of("Location", "/"), null);
        } catch (Exception e) {
            String errorBody = fileContentReader.readStaticResourceToString("/error.html").replaceAll(HtmlFlag.ERROR_MESSAGE.getFlag(), e.getMessage());
            return new MyHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, Map.of("Content-Type", "text/html"), errorBody.getBytes());
        }
    }
}
