package db;

import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static util.Constants.*;

public class ArticleDatabase {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDatabase.class);
    private DatabaseConnectionPool connectionPool;

    public ArticleDatabase() {
        connectionPool = new DatabaseConnectionPool(JDBC_URL);
    }

    public Connection connect() throws SQLException {
        return connectionPool.getConnection();
    }

    public void dropArticleTable() {
        String sql = "DROP ALL OBJECTS;";
        try (Connection conn = connect(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
            logger.info("Article 테이블 삭제 성공");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }


    public void createArticleTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ARTICLE(" +
                "ARTICLE_ID INT AUTO_INCREMENT PRIMARY KEY," +
                "USER_NAME VARCHAR(255)," +
                "TEXT TEXT NOT NULL," +
                "PIC BLOB NOT NULL" +
                ");";

        try (Connection conn = connect(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
            logger.info("Article 테이블 생성 성공");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public void createArticle(String userName, String text, byte[] pic) {
        String sql = "INSERT INTO ARTICLE (ARTICLE_ID, USER_NAME, TEXT, PIC) VALUES (DEFAULT, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, text);
            preparedStatement.setBytes(3, pic);
            preparedStatement.execute();
            logger.info("Article Inserted.");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public List<Article> getAllArticles() {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT * FROM ARTICLE ORDER BY ARTICLE_ID DESC";
        try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                int articleId = rs.getInt("ARTICLE_ID");
                String userName = rs.getString("USER_NAME");
                String text = rs.getString("TEXT");
                byte[] pic = rs.getBytes("PIC");

                articles.add(new Article(articleId, userName, text, pic));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return articles;
    }

    public Optional<Article> getArticle(Integer articleId) {
        String sql = "SELECT * FROM ARTICLE WHERE ARTICLE_ID = ?";

        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, articleId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String userName = rs.getString("USER_NAME");
                String text = rs.getString("TEXT");
                byte[] pic = rs.getBytes("PIC");

                Article article = new Article(articleId, userName, text, pic);
                return Optional.of(article);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

}
