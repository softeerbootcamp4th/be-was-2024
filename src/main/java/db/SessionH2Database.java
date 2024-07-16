package db;

import common.StringUtils;
import model.Session;

import java.sql.*;

public class SessionH2Database {

    private final static String URL = DatabaseInfo.DB_URL.getKey();
    private final static String USERNAME = DatabaseInfo.DB_USERNAME.getKey();
    private final static String PASSWORD = DatabaseInfo.DB_PASSWORD.getKey();

    public static Session createDefaultSession() {
        String SID = StringUtils.createRandomUUID();
        long expireTime = System.currentTimeMillis() + 1800 * 1000;
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO sessions (session_id, age) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, SID);
                preparedStatement.setLong(2, expireTime);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Session(SID, expireTime);
    }

    public static Session findSessionById(String SID) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String query = "SELECT session_id, age FROM sessions WHERE session_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, SID);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String sessionId = resultSet.getString("session_id");
                        long age = Integer.parseInt(resultSet.getString("age"));

                        return new Session(sessionId, age);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeExpiredSessions() {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM sessions WHERE age < ?"
             )) {
                preparedStatement.setLong(1, System.currentTimeMillis());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void invalidateSession(String SID) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE sessions SET age = -1 WHERE session_id = ?"
             )) {
            statement.setString(1, SID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidatedSession(String SID) {
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM sessions WHERE session_id = ? AND age != -1"
             )) {
            statement.setString(1, SID);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
