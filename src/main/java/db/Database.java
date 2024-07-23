package db;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * h2 Database에 직접 User 정보 접근을 위한 DB
 */
public class Database {

    /**
     * User 정보를 Database에 저장하는 메서드
     * @param user 저장하고자 하는 User 객체
     */
    public static void addUser(User user) {
        String sql = "INSERT INTO USERS (userId, password, name, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * User 정보를 Database에서 탐색하는 메서드
     * @param userId 탐색하고자 하는 유저 정보
     * @return 탐색된 User 객체
     */
    public static User findUserById(String userId) {
        String sql = "SELECT * FROM USERS WHERE userId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getString("userId"),
                            resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 모든 유저 정보를 Database에서 탐색하는 메서드
     * @return 모든 유저의 정보가 담긴 Map
     */
    public static Map<String, User> findAll() {
        Map<String, User> users = new HashMap<>();
        String sql = "SELECT * FROM USERS";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
                users.put(user.getUserId(), user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
