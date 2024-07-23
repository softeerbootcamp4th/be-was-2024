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
import webserver.session.SessionDAO;

/**
 * DB의 usertable 에 쿼리를 날리기 위한 클래스
 */
public class UserDAO {
    private Connection conn = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    private final String MEMBER_LIST = "select * from usertable";
    private final String MEMBER_INSERT = "insert into usertable(USERID, USERNAME, EMAIL, PASSWORD) values(?, ?, ?, ?)";
    private String MEMBER_DELETE = "delete usertable where userid = ?";
    private String MEMBER_FIND = "select * from usertable where userid = ?";

    /**
     * 해당 userid를 가진 회원을 삭제한다
     */
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


    /**
     * 회원의 정보를 받아서 저장한다
     * @param userid 사용자의 id
     * @param username 사용자의 이름
     * @param email 사용자의 email
     * @param password 사용자의 password
     * @return 사용자의 User class를 넘겨준다. 저장에 실패했다면 null을 반환한다.
     * @see User
     */
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

    /**
     * userid에 대한 user가 있는지 찾는다
     * @param userid 사용자의 id
     * @return 사용자의 User class를 넘겨준다. 아무도 없다면 null을 반환한다.
     * @see User
     */
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

    /**
     * 회원의 전체 정보들을 array로 반환한다.
     * @return user class의 array를 반환한다.
     * @see User
     */
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