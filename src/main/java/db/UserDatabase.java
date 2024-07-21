package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static util.Constants.JDBC_URL;


/**
 * 사용자의 정보를 저장하는 데이터베이스 클래스입니다.
 */
public class UserDatabase {
    private static final Logger logger = LoggerFactory.getLogger(UserDatabase.class);
    private static DatabaseConnectionPool connectionPool;

    /**
     * JDBC_URL에 연결된 커넥션 풀을 주입합니다.
     */
    public UserDatabase() {
        connectionPool = new DatabaseConnectionPool(JDBC_URL);
    }

    /**
     * connectionPool을 이용해 connection을 사용합니다.
     * @return connectionPool에서 현재 사용 가능한 커넥션을 반환합니다.
     * @throws SQLException SQL문 오류 시 발생합니다.
     */
    public static Connection connect() throws SQLException {
        return connectionPool.getConnection();
    }

    /**
     * 데이터베이스에 users 테이블이 존재하지 않는다면 생성합니다.
     * users 테이블은 다음과 같은 필드를 갖습니다.
     * <pre>
     *     user_id: varchar(255), primary key, 유저 아이디
     *     name: varchar(255), 유저 이름
     *     password: varchar(255), 유저 비밀번호
     *     email: varchar(255), 유저 이메일
     * </pre>
     */
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

    /**
     * 데이터베이스에 새 유저를 추가합니다.
     * @param user 데이터베이스에 추가할 User 인스턴스
     */
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

    /**
     * userId를 이용해 user를 찾습니다.
     * @param userId 찾고자 하는 유저의 userId
     * @return 해당 userId를 가진 유저를 찾았을 경우, 해당 유저 객체를 Optional을 이용해 반환합니다. 그러한 유저가 없을 경우 빈 Optional을 반환합니다.
     */
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

    /**
     * 데이터베이스에 등록된 전체 유저를 반환하는 메서드입니다.
     * @return Collection<User>를 반환합니다.
     */
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
