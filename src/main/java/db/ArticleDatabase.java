package db;

import model.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static util.Constants.*;

/**
 * 게시글을 저장하는 데이터베이스입니다.
 */

public class ArticleDatabase {
    private static final Logger logger = LoggerFactory.getLogger(ArticleDatabase.class);
    private DatabaseConnectionPool connectionPool;

    /**
     * JDBC_URL에 연결된 커넥션 풀을 주입합니다.
     */
    public ArticleDatabase() {
        connectionPool = new DatabaseConnectionPool(JDBC_URL);
    }

    /**
     * connectionPool을 이용해 connection을 사용합니다.
     * @return connectionPool에서 현재 사용 가능한 커넥션을 반환합니다.
     * @throws SQLException SQL문 오류 시 발생합니다.
     */
    public Connection connect() throws SQLException {
        return connectionPool.getConnection();
    }

    /**
     * 데이터베이스 안의 모든 데이터를 삭제합니다.
     */
    public void dropAllObjects() {
        String sql = "DROP ALL OBJECTS;";
        try (Connection conn = connect(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
            logger.info("Article 테이블 삭제 성공");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Article 테이블을 생성합니다.
     * Article 테이블은 다음과 같은 필드를 갖습니다.
     *<pre>
     *Article_id: int, auto_increment, primary key
     *user_name: varchar(255)
     *text: text, not null
     *pic: blob, not null</pre>
     *
     */
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

    /**
     * 게시글을 생성하는 메서드입니다.
     * @param userName 작성자 이름
     * @param text 게시글 본문
     * @param pic 게시글 이미지
     */
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

    /**
     * 전체 게시글을 반환하는 메서드입니다.
     * @return Article 클래스를 List 형식을 이용하여 반환합니다.
     */
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

    /**
     * 게시글을 단건 조회하는 메서드입니다.
     * @param articleId 조회할 게시글의 id
     * @return Article 클래스를 Optional을 이용하여 반환합니다.
     */
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
