package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static util.Constants.JDBC_URL;

public class UserDatabase {
    private static final Logger logger = LoggerFactory.getLogger(UserDatabase.class);
    private static DatabaseConnectionPool connectionPool;

    public UserDatabase() {
        connectionPool = new DatabaseConnectionPool(JDBC_URL);
    }

    public static Connection connect() throws SQLException {
        return connectionPool.getConnection();
    }

    public void createUserTable() {
        String sql = "CREATE TABLE IF NOT EXISTS USERS(" +
                "USER_ID VARCHAR(255) PRIMARY KEY," +
                "NAME VARCHAR(255)," +
                "PASSWORD VARCHAR(255)," +
                "EMAIL VARCHAR(255));";

        try (Connection conn = connect(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
            logger.info("Users 테이블 생성 성공");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void addUser(User user) {
        String sql = "INSERT INTO USERS (USER_ID, NAME, PASSWORD, EMAIL) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.execute();
            logger.info("User Inserted.");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public Optional<User> findUserById(String userId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";

        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String password = rs.getString("PASSWORD");
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");

                return Optional.of(new User(userId, name, password, email));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }


    public Collection<User> findAll() {
        Collection<User> users = new ArrayList<>();
        String sql = "SELECT * FROM USERS";
        try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                String userId = rs.getString("USER_ID");
                String password = rs.getString("PASSWORD");
                String name = rs.getString("NAME");
                String email = rs.getString("EMAIL");

                users.add(new User(userId, name, password, email));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return users;
    }
}
