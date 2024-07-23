package db;


import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * User객체를 h2데이터베이스에 추가하고 얻어오는 클래스
 */
public class UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

    /**
     * H2 데이터베이스에 User를 추가한다
     * @param user 추가하고자 하는 User객체
     */
    public void addUser(User user)  {
        String sql = "INSERT INTO users (user_id, password, name, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            logger.debug(user.toString());
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            logger.error("SQL Exception while adding user : {}" , e.getMessage());
        } catch(Exception e)
        {
            logger.debug(e.getMessage());
        }
    }


    /**
     * Id로 User를 데이터베이스에서 찾아서 반환해주는 메소드
     * @param userId 찾고자 하는 User객체의 Id
     */
    public User findUserById(String userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                );
            }
            return null;
        }
    }

    /**
     * 데이터베이스에서 모든 User객체를 찾아서 List에 담아서 반환해주는 메소드
     */
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getString("user_id"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        }
        return users;
    }
}
