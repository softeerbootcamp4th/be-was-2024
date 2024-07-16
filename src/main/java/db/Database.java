package db;

import model.Article;
import model.User;
import util.ConstantUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Database에 접근하여 데이터를 처리하는 클래스
 */
public class Database {

    private Database() {
    }

    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    /**
     * h2 DB 테이블 생성
     */
    static {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {

            String createUserTableSQL = "CREATE TABLE IF NOT EXISTS \"User\" (" +
                    "userId VARCHAR(255) PRIMARY KEY, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "name VARCHAR(255) NOT NULL UNIQUE, " +
                    "email VARCHAR(255) NOT NULL" +
                    ")";

            String createArticleTableSQL = "CREATE TABLE IF NOT EXISTS \"Article\" (" +
                    "articleId INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(255) NOT NULL, " +
                    "contents VARCHAR(255) NOT NULL, " +
                    "authorName VARCHAR(255) NOT NULL" +
                    ")";

            /*
            String deleteUserTableSQL = "DROP TABLE IF EXISTS \"User\"";
            String deleteArticleTableSQL = "DROP TABLE IF EXISTS \"Article\"";
            statement.execute(deleteUserTableSQL);
            statement.execute(deleteArticleTableSQL);
             */
            statement.execute(createUserTableSQL);
            statement.execute(createArticleTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * User 객체를 DB에 추가하는 메서드
     * @param user
     */
    public static void addUser(User user) {
        String sql = "INSERT INTO \"User\" (userId, password, name, email) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getName());
            pstmt.setString(4, user.getEmail());
            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Article 객체를 DB에 추가하는 메서드
     * @param article
     * @return articleId
     */
    public static Article addArticle(Article article) {
        String sql = "INSERT INTO \"Article\" (title, contents, authorName) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, article.getTitle());
            pstmt.setString(2, article.getContent());
            pstmt.setString(3, article.getAuthorName());
            pstmt.execute();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return Article.of(id + "", article.getTitle(), article.getContent(), article.getAuthorName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

        /**
     * userId로 User 객체를 조회하는 메서드
     * @param userId
     * @return User
     */
    public static Optional<User> findUserById(String userId) {
        String sql = "SELECT * FROM \"User\" WHERE userId = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = User.of(
                        rs.getString(ConstantUtil.USER_ID),
                        rs.getString(ConstantUtil.PASSWORD),
                        rs.getString(ConstantUtil.NAME),
                        rs.getString(ConstantUtil.EMAIL)
                );
                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * articleId로 Article 객체를 조회하는 메서드
     * @param articleId
     * @return Article
     */
    public static Optional<Article> findArticleById(int articleId) {
        String sql = "SELECT * FROM \"Article\" WHERE articleId = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, articleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Article article = Article.of(
                        rs.getString(ConstantUtil.ARTICLE_ID),
                        rs.getString(ConstantUtil.TITLE),
                        rs.getString(ConstantUtil.CONTENT),
                        rs.getString(ConstantUtil.AUTHOR_NAME)
                );
                return Optional.of(article);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * 로그인 요청 처리 후 User 객체를 반환하는 메서드
     * @param userId
     * @param password
     * @return User
     */
    public static Optional<User> login(String userId, String password) {
        String sql = "SELECT * FROM \"User\" WHERE userId = ? AND password = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = User.of(
                        rs.getString(ConstantUtil.USER_ID),
                        rs.getString(ConstantUtil.PASSWORD),
                        rs.getString(ConstantUtil.NAME),
                        rs.getString(ConstantUtil.EMAIL)
                );
                return Optional.of(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    /**
     * 모든 User 객체를 조회하는 메서드
     * @return Collection
     */
    public static Collection<User> findAllUser() {
        String sql = "SELECT * FROM \"User\"";
        Collection<User> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = User.of(
                        rs.getString(ConstantUtil.USER_ID),
                        rs.getString(ConstantUtil.PASSWORD),
                        rs.getString(ConstantUtil.NAME),
                        rs.getString(ConstantUtil.EMAIL)
                );
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    /**
     * 모든 Article 객체를 조회하는 메서드
     * @return Collection
     */
    public static Collection<Article> findAllArticles(){
        String sql = "SELECT * FROM \"Article\"";
        Collection<Article> articles = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            while(rs.next()){
                Article article = Article.of(
                        rs.getString(ConstantUtil.ARTICLE_ID),
                        rs.getString(ConstantUtil.TITLE),
                        rs.getString(ConstantUtil.CONTENT),
                        rs.getString(ConstantUtil.AUTHOR_NAME)
                );
                articles.add(article);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    /**
     * User 테이블을 초기화하는 메서드
     */
    public static void clearUsers() {
        String sql = "DELETE FROM \"User\"";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Article 테이블을 초기화하는 메서드
     */
    public static void clearArticles() {
        String sql = "DELETE FROM \"Article\"";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
