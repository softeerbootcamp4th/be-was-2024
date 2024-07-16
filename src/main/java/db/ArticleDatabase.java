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

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, H2_USERNAME, H2_PASSWORD);
    }

    public static void createArticleTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ARTICLE(" +
                "ARTICLE_ID INT AUTO_INCREMENT PRIMARY KEY," +
                "USER_ID VARCHAR(255)," +
                "TEXT VARCHAR(255)," +
                "PIC VARCHAR(255)" +
                ");";

        try (Connection conn = connect(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
            logger.info("Article 테이블 생성 성공");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void createArticle(String userId, String text, String pic) {
        String sql = "INSERT INTO ARTICLE (ARTICLE_ID, USER_ID, TEXT, PIC) VALUES (DEFAULT, ?,?,?)";
        try (Connection conn = connect(); PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, text);
            preparedStatement.setString(3, pic);
            preparedStatement.execute();
            logger.info("Article Inserted.");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static List<Integer> getAllArticles() {
        List<Integer> articleIds = new ArrayList<>();
        String sql = "SELECT * FROM ARTICLE";
        try (Connection connection = connect(); Statement statement = connection.createStatement(); ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("ARTICLE_ID");
                articleIds.add(id);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return articleIds;
    }

    public static Optional<Article> getArticle(Integer articleId) {
        String sql = "SELECT * FROM ARTICLE WHERE ARTICLE_ID = ?";

        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, articleId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String userId = rs.getString("USER_ID");
                String text = rs.getString("TEXT");
                String pic = rs.getString("PIC");

                Article article = new Article(userId, text, pic);
                return Optional.of(article);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }

}
