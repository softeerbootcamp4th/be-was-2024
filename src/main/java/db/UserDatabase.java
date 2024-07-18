package db;

import model.User;

import java.util.*;
import java.sql.*;

public class UserDatabase {
    private static final String dbUrl = "jdbc:h2:~/test";
    private static final String dbUsername = "sa";
    private static final String dbPassword = "";

    public static void addUser(User user) {
        String sqlInsert = "INSERT INTO Users (userId, username, password, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User findUserById(String userId) {
        User foundUser = null;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            System.out.println("H2 데이터베이스에 성공적으로 연결되었습니다.");

            // SELECT 쿼리를 사용하여 사용자 조회
            String sqlSelect = "SELECT * FROM Users WHERE userId = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelect)) {
                pstmt.setString(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // 사용자 데이터를 가져와서 User 객체로 변환
                        User user = new User.Builder()
                                .userId(rs.getString("userId"))
                                .username(rs.getString("username"))
                                .password(rs.getString("password"))
                                .email(rs.getString("email"))
                                .build();

                        foundUser = user;
                        System.out.println("찾은 사용자 정보: " + user);
                    } else {
                        System.out.println("해당 userId를 가진 사용자가 없습니다.");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("H2 데이터베이스 연결 실패:");
            e.printStackTrace();
        }

        return foundUser;
    }

    public static Collection<User> findAll() {
        List<User> userList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // SELECT 쿼리를 사용하여 모든 사용자 조회
            String sqlSelectAll = "SELECT * FROM Users";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelectAll);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User.Builder()
                            .userId(rs.getString("userId"))
                            .username(rs.getString("username"))
                            .password(rs.getString("password"))
                            .email(rs.getString("email"))
                            .build();

                    userList.add(user);
                }
            }

        } catch (SQLException e) {
            System.out.println("H2 데이터베이스 연결 실패:");
            e.printStackTrace();
        }

        return userList;
    }
}
