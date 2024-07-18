package db.tables;

import db.DBUtil;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class UserTable {
    private final static String insertSQL = "insert into member(userId, password, name, email) values(?, ?, ?, ?)";
    private final static String findAllSQL = "select userId, password, name, email from member";
    private final static String findByIdSQL = "select userId, password, name, email from member where userId = ?";

    public static void addUser(User user) {
        DBUtil.statement(conn -> {
            ResultSet keys = null;
            try(
                    PreparedStatement pstmt = conn.prepareStatement(insertSQL);
            ) {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());

                pstmt.executeUpdate();
                keys = pstmt.getGeneratedKeys();
                if(keys.next()) user.setUserId(keys.getString(1));
            } catch(Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if(keys != null) keys.close();
                } catch(Exception e) {

                }
            }
        });
    }

    public static List<User> findAll() {
        return DBUtil.query((conn) -> {
            try(
                    PreparedStatement stmt = conn.prepareStatement(findAllSQL);
                    ResultSet rs = stmt.executeQuery();
            ) {
                List<User> users = new ArrayList<>();

                while(rs.next()) {
                    User user = new User(
                            rs.getString("userId"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                    users.add(user);
                }
                return users;
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static User findUserById(String userId) {
        return DBUtil.query((conn) -> {
            try(PreparedStatement stmt = conn.prepareStatement(findByIdSQL)) {
                stmt.setString(1, userId);
                User user = null;

                try (ResultSet rs = stmt.executeQuery()) {
                    if(rs.next()) {
                        user = new User(
                                rs.getString("userId"),
                                rs.getString("password"),
                                rs.getString("name"),
                                rs.getString("email")
                        );
                    }
                }

                return user;
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
