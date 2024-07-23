package repository;

import constant.FileExtensionType;
import exception.DatabaseException;
import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Optional;

/**
 * 게시글 정보의 CRUD를 담당하는 클래스
 */
public class PostRepository {
    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);

    /**
     * 게시글을 저장하는 테이블을 DB에 생성한다.
     * 이미 테이블이 존재한다면 생성하지 않는다.
     *
     * @param connection : H2 DB Connection 객체
     */
    public static void initialize(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS posts (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255) NOT NULL," +
                    "content TEXT NOT NULL," +
                    "image BLOB," +
                    "fileType VARCHAR(20)," +
                    "userId INT NOT NULL," +
                    "FOREIGN KEY (userId) REFERENCES users(id)" +
                    ")");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("Initializaion failed in PostRepository");
        }
    }

    /**
     * 게시글 정보를 DB에 저장한다.
     *
     * @param post : 저장할 게시글
     * @param connection : H2 DB Connection 객체
     */
    public static void addPost(Post post, Connection connection) {
        String sql = "INSERT INTO posts (title, content, image, fileType, userId) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getContent());
            preparedStatement.setBytes(3, post.getImage());
            preparedStatement.setString(4, post.getFileType().name());
            preparedStatement.setInt(5, post.getUserId());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt(1)); // 자동 생성된 id 값을 설정합니다.
                } else {
                    throw new DatabaseException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("addPost failed in PostRepository");
        }
    }

    /**
     * 게시글 id로 게시글 데이터를 찾아 객체로 변환하여 반환한다.
     *
     * @param postId : 게시글의 id
     * @param connection : H2 DB Connection 객체
     * @return : 게시글 객체
     */
    public static Post findById(int postId, Connection connection) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Post(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getBytes("image"),
                        FileExtensionType.valueOf(resultSet.getString("fileType")),
                        resultSet.getInt("userId")
                );
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("findById failed in PostRepository");
        }
        return null;
    }

    /**
     * 게시글 id 값보다 작은 게시물 중, 가장 큰 id 값을 가진 게시글 반환
     *
     * @param postId : 게시글 id
     * @param connection : H2 DB Connection 객체
     * @return : 조건을 만족하는 게시글이 없을 수 있으므로 Optional<Post> 반환
     */
    public static Optional<Post> findPreviousPost(int postId, Connection connection) {
        String sql = "SELECT * FROM posts WHERE id < ? ORDER BY id DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Post(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getBytes("image"),
                        FileExtensionType.valueOf(resultSet.getString("fileType")),
                        resultSet.getInt("userId")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("findPreviousPost failed in PostRepository");
        }
    }

    /**
     * 게시글 id 값보다 큰 게시물 중, 가장 작은 id 값을 가진 게시글 반환
     *
     * @param postId : 게시글 id
     * @param connection : H2 DB Connection 객체
     * @return : 조건을 만족하는 게시글이 없을 수 있으므로 Optional<Post> 반환
     */
    public static Optional<Post> findNextPost(int postId, Connection connection) {
        String sql = "SELECT * FROM posts WHERE id > ? ORDER BY id ASC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Post(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("content"),
                        resultSet.getBytes("image"),
                        FileExtensionType.valueOf(resultSet.getString("fileType")),
                        resultSet.getInt("userId")
                ));
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new DatabaseException("findNextPost failed in PostRepository");
        }
    }

}
