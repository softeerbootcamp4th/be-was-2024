package db;

import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JdbcDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 게시글 데이터베이스
 */
public class PostDatabase {
    private static final Logger logger = LoggerFactory.getLogger(PostDatabase.class);

    public static void addPost(Post post){
        String query = "INSERT INTO POST (AUTHOR_ID, AUTHOR_NAME, CONTENT) VALUES (?, ?, ?)";
        try (Connection connection = JdbcDatabase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1,post.getAuthorId());
            preparedStatement.setString(2,post.getAuthorName());
            preparedStatement.setString(3,post.getContent());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("sql error while adding post");
        }
    }

    public static Post getPost(Long postId){
        String query = "SELECT * FROM POST WHERE AUTHOR_ID = ?";
        try (Connection connection = JdbcDatabase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1,postId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String authorId = resultSet.getString("AUTHOR_ID");
                    String authorName = resultSet.getString("AUTHOR_NAME");
                    String content = resultSet.getString("CONTENT");
                    return new Post(Long.parseLong(authorId), authorName, content);
                }
            }
        } catch (SQLException e) {
            logger.error("sql error while adding post");
        }
        return null;
    }

    public static Long getMinimumPostId(){
        String query = "SELECT MIN(ID) FROM POST";
        Long minId = null;
        try (Connection connection = JdbcDatabase.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                minId = resultSet.getLong(1);
            }

        } catch (SQLException e) {
            logger.error("sql error while adding post");
        }
        return minId;
    }
}
