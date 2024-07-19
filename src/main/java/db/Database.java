package db;

import handler.PostHandler;
import model.Board;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class Database {
    private static final UserDao userDao = new UserDao();
    private static final BoardDao boardDao = new BoardDao();

    public static void addUser(User user) throws SQLException {
        userDao.addUser(user);
    }

    public static User findUserById(String userId) throws SQLException {
        return userDao.findUserById(userId);
    }

    public static List<User> findAll() throws SQLException {
        return userDao.findAll();
    }

    public static void addBoard(Board board) throws SQLException {
        boardDao.addBoard(board);
    }

    public static List<Board> getBoards() throws SQLException {
        return boardDao.getBoards();
    }

    public static Board getOneBoard(String title) throws SQLException
    {
        return boardDao.getOneBoard(title);
    }
}