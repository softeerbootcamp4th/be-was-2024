package db;

import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class UserTable {
    private final static String createSQL = "insert into member(userId, password, name, email) values(?, ?, ?, ?)";
    private final static String findAllSQL = "select * from member";
    private final static String findByIdSQL = "select * from member where userId = ?";

    public static void addUser(User user) {
        DBUtil.statement(conn -> {
            try(
                    PreparedStatement pstmt = conn.prepareStatement(createSQL);
            ) {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, user.getEmail());

                pstmt.executeUpdate();;
            } catch(Exception e) {
                throw new RuntimeException(e);
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
