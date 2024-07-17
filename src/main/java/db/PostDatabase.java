package db;

import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JdbcDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 게시글 데이터베이스
 */
public class PostDatabase {
    private static final Logger logger = LoggerFactory.getLogger(PostDatabase.class);

    public static void addPost(Post post){
        String query = "INSERT INTO POST (AUTHOR_ID, CONTENT) VALUES (?, ?)";
        try (Connection connection = JdbcDatabase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1,post.getAuthorId());
            preparedStatement.setString(2,post.getContent());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("sql error while adding post");
        }
    }

    public static void getPost(int postId){
        String query = "SELECT * FROM POST WHERE AUTHOR_ID = ?";
    }
}
