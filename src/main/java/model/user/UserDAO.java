package model.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import db.JDBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDAO {
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private final String MEMBER_LIST = "select * from usertable";
    private final String MEMBER_INSERT = "insert into usertable(USERID, USERNAME, EMAIL, PASSWORD) values(?, ?, ?, ?)"; //userid, username, email, password
    private String MEMBER_DELETE = "delete usertable where userid = ?";
    private String MEMBER_FIND = "select * from usertable where userid = ?";

    // 회원 삭제
    public void deleteUser(String userid) {
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(MEMBER_DELETE);

            stmt.setString(1, userid);
            stmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
        } finally {
            JDBC.close(stmt, conn);
        }
    }

    // 회원 삽입
    public User insertUser(String userid, String username, String email, String password) {
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(MEMBER_INSERT);


            stmt.setString(1, userid);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            JDBC.close(stmt, conn);
        }
        return new User(userid, username, email, password);
    }

    // 회원 검색
    public User getUser(String userid) {
        User user = null;
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(MEMBER_FIND);

            stmt.setString(1, userid);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                user = new User(userid, password, name, email);
            }

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            JDBC.close(rs, stmt, conn);
        }
        return user;
    }

    // 회원 목록
    public ArrayList<User> getUserList() {
        ArrayList<User> userlist = new ArrayList<User>();
        try {
            conn = JDBC.getConnection();
            stmt = conn.prepareStatement(MEMBER_LIST);
            rs = stmt.executeQuery();

            if (rs.next()) {
                do {
                    String userid = rs.getString("userid");
                    String name = rs.getString("username");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    userlist.add(new User(userid, password, name, email));
                } while (rs.next());
            }

        } catch (SQLException e) {
            logger.error("error{}", e.getMessage());
            logger.error(Arrays.toString(e.getStackTrace()));
            return null;
        } finally {
            JDBC.close(rs, stmt, conn);
        }
        return userlist;
    }

}