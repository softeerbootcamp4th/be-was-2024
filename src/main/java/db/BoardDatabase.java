package db;

import model.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * h2 Database에 직접 Board 정보 접근을 위한 DB
 */
public class BoardDatabase {

    /**
     * Board 정보를 Database에 저장하는 메서드
     * @param board 저장하고자 하는 Board 객체
     */
    public static void addBoard(Board board) {
        String sql = "INSERT INTO BOARD (userId, content, filePath) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, board.getUserId());
            statement.setString(2, board.getContent());
            statement.setString(3, board.getFilePath()); // 파일 경로 추가
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Board findBoardById(String boardId) {
        String sql = "SELECT * FROM BOARD WHERE boardId = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, boardId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Retrieve board details
                String userId = resultSet.getString("userId");
                String content = resultSet.getString("content");
                String filePath = resultSet.getString("filePath");
                // Create and return Board object
                return new Board(userId, content, filePath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Board not found
    }

    public static List<Board> findAllBoards() {
        List<Board> boards = new ArrayList<>();
        String sql = "SELECT * FROM BOARD";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String userId = resultSet.getString("userId");
                String content = resultSet.getString("content");
                String filePath = resultSet.getString("filePath");
                Board board = new Board(userId, content, filePath);
                boards.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }
}
