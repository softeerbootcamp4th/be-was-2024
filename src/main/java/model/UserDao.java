package model;

import db.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserDao {

    private static final DataSource dataSource = DataSource.getInstance();

    public static void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS USERS ("
                + "USERID VARCHAR(255) PRIMARY KEY, "
                + "PASSWORD VARCHAR(255), "
                + "NAME VARCHAR(255), "
                + "EMAIL VARCHAR(255))";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    public void addUser(User user) throws SQLException {
        String insertSQL = "INSERT INTO USERS (USERID, PASSWORD, NAME, EMAIL) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.execute();
        }
    }

    public User getUser(String userId) throws SQLException {
        String selectSQL = "SELECT * FROM USERS WHERE USERID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String password = resultSet.getString("PASSWORD");
                    String name = resultSet.getString("NAME");
                    String email = resultSet.getString("EMAIL");
                    return new User(userId, password, name, email);
                }
            }
        }

        return null;
    }

    // 모든 사용자 조회 메서드
    public Collection<User> getAllUsers() throws SQLException {
        String selectSQL = "SELECT * FROM USERS";
        Collection<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String userId = resultSet.getString("USERID");
                String password = resultSet.getString("PASSWORD");
                String name = resultSet.getString("NAME");
                String email = resultSet.getString("EMAIL");
                users.add(new User(userId, password, name, email));
            }
        }
        return users;
    }


    // 사용자 삭제 메서드
    public void deleteUser(String userId) throws SQLException {
        String deleteSQL = "DELETE FROM USERS WHERE USERID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setString(1, userId);
            preparedStatement.executeUpdate();
        }
    }

    public boolean userExists(String userId) throws SQLException {
        String checkSQL = "SELECT 1 FROM USERS WHERE USERID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(checkSQL)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // 만약 결과가 있으면 유저가 존재하는 것
                return resultSet.next();
            }
        }
    }

    // 사용자 ID로 사용자 찾기 메서드
    public User findUserById(String userId) throws SQLException {
        String selectSQL = "SELECT * FROM USERS WHERE USERID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String password = resultSet.getString("PASSWORD");
                    String name = resultSet.getString("NAME");
                    String email = resultSet.getString("EMAIL");
                    return new User(userId, password, name, email);
                }
            }
        }
        return null;
    }

    public void deleteAllUsers() throws SQLException {
        String deleteAllSQL = "DELETE FROM USERS";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteAllSQL)) {
            preparedStatement.executeUpdate();
        }
    }
}
