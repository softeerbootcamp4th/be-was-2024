package db;

import model.Article;
import model.User;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<Integer, Article> articles = new ConcurrentHashMap<>();

    public static boolean addUser(User user) {
        try (Connection conn = JDBCUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (userId, password, name, email) values (?, ?, ?, ?)")) {

            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getEmail());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static User findUserById(String userId) {
        try (Connection conn = JDBCUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE userId = ?")) {

            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("userId"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Collection<User> findAll() {
        ArrayList<User> users = new ArrayList<>();
        try (Connection conn = JDBCUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users;")) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                 users.add(new User(
                        rs.getString("userId"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static boolean addSession(String sessionId, String userId) {
        if (sessions.contains(sessionId)) return false;
        sessions.put(sessionId, userId);
        return true;
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static String findUserIdBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    public static boolean addArticle(Article article) {
        try (Connection conn = JDBCUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO articles (userId, content, image) values (?, ?, ?)")) {

            stmt.setString(1, article.getUserId());
            stmt.setString(2, article.getContent());
            stmt.setString(3, article.getImage());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Article getArticleById(Integer articleId) {
        try (Connection conn = JDBCUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM articles WHERE id = ?")) {

            stmt.setInt(1, articleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Article(
                        rs.getString("userId"),
                        rs.getString("content"),
                        rs.getString("image")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
