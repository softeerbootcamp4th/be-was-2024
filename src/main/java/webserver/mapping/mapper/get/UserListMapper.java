package webserver.mapping.mapper.get;

import db.ConnectionPool;
import model.User;
import webserver.annotation.LoginCheck;
import webserver.enums.HtmlFlag;
import webserver.enums.HttpStatus;
import webserver.http.MyHttpRequest;
import webserver.http.MyHttpResponse;
import webserver.mapping.mapper.HttpMapper;
import webserver.util.FileContentReader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@LoginCheck
public class UserListMapper implements HttpMapper {
    ConnectionPool databaseConnections = ConnectionPool.getInstance();
    FileContentReader fileContentReader = FileContentReader.getInstance();

    @Override
    public MyHttpResponse handle(MyHttpRequest httpRequest) throws IOException {
        try {
            return getUserList();
        } catch (Exception e) {
            return new MyHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, Map.of(), "Internal Server Error".getBytes());
        }
    }

    public MyHttpResponse getUserList() throws IOException {
        Connection connection = databaseConnections.getConnection();
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery();) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                users.add(user);
            }

        } catch (Exception e) {
            String errorBody = fileContentReader.readStaticResourceToString("/error.html").replaceAll(HtmlFlag.ERROR_MESSAGE.getFlag(), e.getMessage());
            return new MyHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, Map.of("Content-Type", "text/html"), errorBody.getBytes());
        }

        databaseConnections.releaseConnection(connection);

        StringBuilder userList = new StringBuilder();

        for (User user : users) {
            userList.append("<li>");
            userList.append(user.getName()).append(" : ");
            userList.append(user.getEmail());
            userList.append("</li>");
        }

        String contentBody = fileContentReader.readStaticResourceToString("/userList.html").replaceAll(HtmlFlag.CONTENT.getFlag(), userList.toString());
        return new MyHttpResponse(HttpStatus.OK, Map.of(), contentBody.getBytes());
    }
}
