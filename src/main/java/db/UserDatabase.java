package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JdbcDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDatabase {
    private static final Logger logger = LoggerFactory.getLogger(UserDatabase.class);

    public static void addUser(User user) {
        String query = "INSERT INTO MEMBER (user_id, password, name, email) VALUES (?, ? ,? ,?)";
        try (Connection connection = JdbcDatabase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Database error in addUser: " + e.getMessage(), e);
        }
    }

    public static User findUserById(String userId) {
        String query = "SELECT * FROM MEMBER WHERE USER_ID = ?";
        try (Connection connection = JdbcDatabase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("ID");
                    String userId1 = resultSet.getString("USER_ID");
                    String password1 = resultSet.getString("PASSWORD");
                    String name = resultSet.getString("NAME");
                    String email = resultSet.getString("EMAIL");
                    return new User(id,userId1, password1, name, email);
                }
            }
        } catch (SQLException e) {
            logger.error("Database error in findUserById: " + e.getMessage(), e);
        }
        return null;
    }


    public static List<String> findAll() {
        String query = "SELECT * FROM MEMBER";
        List<String> users = new ArrayList<>();
        try (Connection connection = JdbcDatabase.getConnection();
             Statement statement = connection.createStatement()){
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    String name = resultSet.getString("NAME");
                    users.add(name);
                }
            }
        } catch (SQLException e) {
            logger.error("Database error in findUserById: " + e.getMessage(), e);
        }
        return users;
    }
}
