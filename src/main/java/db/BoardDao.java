package db;

import model.Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BoardDao {
    public void addBoard(Board board) throws SQLException {
        String sql = "INSERT INTO boards (title, content, image) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, board.getTitle());
            pstmt.setString(2, board.getContent());
            pstmt.setBytes(3,board.getImage());
            pstmt.execute();
        }
    }

    public List<Board> getBoards() throws SQLException {
        String sql = "SELECT * FROM boards";
        List<Board> boards = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                boards.add(new Board(
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getBytes("image")
                ));
            }
        }
        return boards;
    }

    public Board getOneBoard(String title) throws SQLException{
        String sql = "SELECT * FROM boards WHERE title=?";
        try(Connection connection = DatabaseUtil.getConnection(); PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1,title);
            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    String boardTitle = rs.getString("title");
                    String content = rs.getString("content");
                    byte[] image = rs.getBytes("image");
                    return new Board(boardTitle,content,image);
                } else {
                    return null;
                }
            }
        }
    }
}
