package db;

import model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * H2 Database의 Article 테이블을 조작하기 위한 API
 */
public class ArticleH2Database {

    private final static String URL = DatabaseInfo.DB_URL.getKey();
    private final static String USERNAME = DatabaseInfo.DB_USERNAME.getKey();
    private final static String PASSWORD = DatabaseInfo.DB_PASSWORD.getKey();

    /**
     * Article을 저장합니다.
     * @param article 저장할 Article 객체
     * @param userId Article 작성자
     * @return 저장된 Article 객체
     */
    public static Article createArticle(Article article, String userId) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO articles (user_id, image_path, content) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, userId);
                preparedStatement.setString(2, article.getImagePath());
                preparedStatement.setString(3, article.getContent());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return article;
    }

    /**
     * Article 테이블에 저장된 모든 데이터를 컬렉션으로 반환
     * @return Article 테이블에 저장된 데이터 리스트
     */
    public static Collection<Article> getArticleList() {
        Collection<Article> articles = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT article_id, user_id, image_path, content FROM articles ORDER BY article_id DESC";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String articleId = resultSet.getString("article_id");
                        String userId = resultSet.getString("user_id");
                        String imagePath = resultSet.getString("image_path");
                        String content = resultSet.getString("content");

                        articles.add(new Article(articleId, userId, imagePath, content));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

}
