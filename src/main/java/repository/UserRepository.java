package repository;

import exception.DatabaseException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 유저 정보의 CRUD를 담당하는 클래스
 */
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    /**
     * 유저 정보를 저장하는 테이블을 DB에 생성한다.
     * 이미 테이블이 존재한다면 생성하지 않는다.
     *
     * @param connection : H2 DB Connection 객체
     */
    public static void initialize(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "userId VARCHAR(255) NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "name VARCHAR(255)," +
                    "email VARCHAR(255)" +
                    ")");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("Initializaion failed in UserRepository");
        }
    }

    /**
     * 유저 정보를 DB에 저장한다.
     *
     * @param user : 유저 객체
     * @param connection : H2 DB Connection 객체
     */
    public static void addUser(User user, Connection connection) {
        String sql = "INSERT INTO users (userId, password, name, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getEmail());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1)); // 자동 생성된 id 값을 설정합니다.
                } else {
                    throw new DatabaseException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("addUser failed in UserRepository");
        }
    }

    /**
     * userId 값을 가지는 User 객체를 반환한다.
     *
     * @param userId : 유저의 userId
     * @param connection : H2 DB Connection 객체
     * @return : 유저의 userId 값에 해당하는 User 객체
     */
    public static User findByUserId(String userId, Connection connection) {
        String sql = "SELECT * FROM users WHERE userID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
            }
            return null;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("findByUserId failed in UserRepository");
        }
    }

    /**
     * 유저의 id 값에 해당하는 유저 객체를 반환한다.
     *
     * @param id : 유저의 id
     * @param connection : H2 DB Connection 객체
     * @return : 유저의 id 값에 해당하는 유저 객체
     */
    public static User findById(int id, Connection connection) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                );
            }
            return null;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("findById failed in UserRepository");
        }

    }

    /**
     * 유저의 userId 값에 해당하는 유저 데이터가 DB에 존재하는지를 반환한다.
     *
     * @param userId : 유저의 userId
     * @param connection : H2 DB Connection 객체
     * @return : userId에 해당하는 유저가 있으면 true, 없으면 false
     */
    public static boolean userExists(String userId, Connection connection) {
        String sql = "SELECT COUNT(*) FROM users WHERE userId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("userExists failed in UserRepository");
        }
    }

    /**
     * 모든 유저 정보를 반환한다.
     *
     * @param connection : H2 DB Connection 객체
     * @return : 모든 유저 객체가 저장된 List
     */
    public static List<User> findAllByList(Connection connection) {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("userId"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                ));
            }
            return users;
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("findAllByList failed in UserRepository");
        }
    }
}
