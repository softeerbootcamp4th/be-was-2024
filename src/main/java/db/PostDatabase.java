package db;

import model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDatabase {
    private static final String dbUrl = "jdbc:h2:~/test";
    private static final String dbUsername = "sa";
    private static final String dbPassword = "";

    public static void addPost(Post newPost) {
        String sqlInsert = "INSERT INTO Posts (userId, title, content) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            pstmt.setString(1, newPost.getUserId());
            pstmt.setString(2, newPost.getTitle());
            pstmt.setString(3, newPost.getContent());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> findAllTitleByUserId(String userId) {
        List<String> titles = new ArrayList<>();
        String sqlSelect = "SELECT title FROM Posts WHERE userId = '" + userId + "'";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlSelect)) {

            while (rs.next()) {
                titles.add(rs.getString("title"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return titles;
    }
}
