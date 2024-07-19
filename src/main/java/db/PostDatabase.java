package db;

import model.Post;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

    public static Post findPostByUserIdAndTitle(String userId, String title) {
        userId = URLDecoder.decode(userId, StandardCharsets.UTF_8);
        title = URLDecoder.decode(title, StandardCharsets.UTF_8);

        String sqlSelect = "SELECT * FROM Posts WHERE userId = '" + userId + "' AND title = '" + title + "'";

        Post foundPost = null;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlSelect)) {

            if (rs.next()) {
                foundPost = new Post.Builder()
                        .title(rs.getString("title"))
                        .content(rs.getString("content"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foundPost;
    }
}
