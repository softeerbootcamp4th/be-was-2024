package model;

import db.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class ArticleDao {
    private static final DataSource dataSource = DataSource.getInstance();
    public static void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS ARTICLES ("
                + "ID INT PRIMARY KEY AUTO_INCREMENT, "
                + "AUTHOR VARCHAR(255), "
                + "CONTENT TEXT, "
                + "IMAGE BLOB)";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        }
    }

    public void addArticle(Article article) throws SQLException {
        String insertSQL = "INSERT INTO ARTICLES (AUTHOR, CONTENT, IMAGE) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, article.getAuthor());
            preparedStatement.setString(2, article.getContent());
            preparedStatement.setBytes(3, article.getImage());
            preparedStatement.execute();
        }
    }

    public Article getArticle(int id) throws SQLException {
        String selectSQL = "SELECT * FROM ARTICLES WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String author = resultSet.getString("AUTHOR");
                    String content = resultSet.getString("CONTENT");
                    byte[] image = resultSet.getBytes("IMAGE");
                    return new Article(id, author, content, image);
                }
            }
        }

        return null;
    }

    // 모든 게시글 조회 메서드
    public Collection<Article> getAllArticles() throws SQLException {
        String selectSQL = "SELECT * FROM ARTICLES";
        Collection<Article> articles = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String author = resultSet.getString("AUTHOR");
                String content = resultSet.getString("CONTENT");
                byte[] image = resultSet.getBytes("IMAGE");
                articles.add(new Article(id, author, content, image));
            }
        }
        return articles;
    }

    // 게시글 삭제 메서드
    public void deleteArticle(int id) throws SQLException {
        String deleteSQL = "DELETE FROM ARTICLES WHERE ID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}
