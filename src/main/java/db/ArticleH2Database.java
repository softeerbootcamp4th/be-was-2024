package db;

import model.Article;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class ArticleH2Database {

    private final static String URL = DatabaseInfo.DB_URL.getKey();
    private final static String USERNAME = DatabaseInfo.DB_USERNAME.getKey();
    private final static String PASSWORD = DatabaseInfo.DB_PASSWORD.getKey();

    public static Article createArticle(Article article) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO articles (image_path, content) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, article.getImagePath());
                preparedStatement.setString(2, article.getContent());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return article;
    }

    public static Collection<Article> getArticleList() {
        Collection<Article> articles = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT article_id, image_path, content FROM articles ORDER BY article_id DESC";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String articleId = resultSet.getString("article_id");
                        String imagePath = resultSet.getString("image_path");
                        String content = resultSet.getString("content");

                        articles.add(new Article(articleId, imagePath, content));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

}
