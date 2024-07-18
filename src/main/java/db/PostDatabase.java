package db;

import model.Post;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
