package db;

import model.Post;
import model.User;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostDatabase {
    private static final String dbUrl = "jdbc:h2:~/test";
    private static final String dbUsername = "sa";
    private static final String dbPassword = "";

    public static void addPost(Post newPost) {
        String sqlInsert = "INSERT INTO Posts (userId, title, content, path) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            pstmt.setString(1, newPost.getUserId());
            pstmt.setString(2, newPost.getTitle());
            pstmt.setString(3, newPost.getContent());
            pstmt.setString(4, newPost.getPath());

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
                        .path(rs.getString("path"))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return foundPost;
    }

    public static Collection<Post> findAll() {
        List<Post> postList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // SELECT 쿼리를 사용하여 모든 사용자 조회
            String sqlSelectAll = "SELECT * FROM Posts";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlSelectAll);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Post post = new Post.Builder()
                            .postId(rs.getInt("postId"))
                            .title(rs.getString("title"))
                            .content(rs.getString("content"))
                            .userId(rs.getString("userId"))
                            .build();

                    postList.add(post);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return postList;
    }

    public static Integer findPrevPostId(int postId) {
        String sqlPrev = "SELECT postId FROM Posts WHERE postId < ? ORDER BY postId DESC LIMIT 1";

        Integer prevPostId = null;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // Find the previous post
            try (PreparedStatement pstmt = conn.prepareStatement(sqlPrev)) {
                pstmt.setInt(1, postId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        prevPostId = rs.getInt("postId");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prevPostId;
    }

    public static Integer findNextPostId(int postId) {
        String sqlNext = "SELECT postId FROM Posts WHERE postId > ? ORDER BY postId ASC LIMIT 1";

        Integer nextPostId = null;

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            // Find the previous post
            try (PreparedStatement pstmt = conn.prepareStatement(sqlNext)) {
                pstmt.setInt(1, postId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        nextPostId = rs.getInt("postId");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nextPostId;
    }
}
