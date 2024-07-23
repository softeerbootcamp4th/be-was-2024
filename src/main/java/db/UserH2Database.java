package db;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserH2Database {

    private final static String URL = DatabaseInfo.DB_URL.getKey();
    private final static String USERNAME = DatabaseInfo.DB_USERNAME.getKey();
    private final static String PASSWORD = DatabaseInfo.DB_PASSWORD.getKey();

    public static User addUser(User user) {
        if(user.getUserId()==null || user.getPassword()==null || user.getName()==null) return null;

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO users (user_id, password, name, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, user.getUserId());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getName());
                preparedStatement.setString(4, user.getEmail());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    // users; drop table users; select * from users
    // where 1=1
    public static User findUserById(String findUserId) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT user_id, password, name, email FROM users WHERE user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, findUserId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String userId = resultSet.getString("user_id");
                        String userPassword = resultSet.getString("password");
                        String userName = resultSet.getString("name");
                        String userEmail = resultSet.getString("email");

                        // User 객체 생성
                        return new User(userId, userPassword, userName, userEmail);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Collection<User> findAll() {
        Collection<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT user_id, password, name, email FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String userId = resultSet.getString("user_id");
                        String userPassword = resultSet.getString("password");
                        String userName = resultSet.getString("name");
                        String userEmail = resultSet.getString("email");

                        // User 객체 생성 및 컬렉션에 추가
                        users.add(new User(userId, userPassword, userName, userEmail));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void removeAll() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "DELETE FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
