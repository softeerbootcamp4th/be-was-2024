package db;

import model.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * h2 Database에 직접 Board 정보 접근을 위한 DB
 */
public class BoardDatabase {

    /**
     * Board 정보를 Database에 저장하는 메서드
     * @param board 저장하고자 하는 Board 객체
     */
    public static void addBoard(Board board) {
        String sql = "INSERT INTO BOARD (userId, content) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, board.getUserId());
            statement.setString(2, board.getContent());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
