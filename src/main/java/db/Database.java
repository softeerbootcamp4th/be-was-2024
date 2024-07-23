package db;

import handler.PostHandler;
import model.Board;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * 유저 객체와 board 객체를 각 dao에 넘겨주는 클래스
 */

public class Database {
    private static final UserDao userDao = new UserDao();
    private static final BoardDao boardDao = new BoardDao();


    /**
     * 유저를 UserDao에 넘겨서 추가해주는 메소드
     * @param user  추가하고자 하는 user
     */
    public static void addUser(User user) throws SQLException {
        userDao.addUser(user);
    }


    /**
     * ID값으로 userDAO에서 해당하는 user를 찾아서 반환해주는 메소드
     * @param userId 찾고자 하는 user의 id
     */
    public static User findUserById(String userId) throws SQLException {
        return userDao.findUserById(userId);
    }

    /**
     * 모든 user객체를 List에 담아서 반환한다.
     */
    public static List<User> findAll() throws SQLException {
        return userDao.findAll();
    }


    /**
     * board객체를 boardDAO에 넘겨주며 추가한다
     * @param board  추가하고자 하는 Board 객체
     */
    public static void addBoard(Board board) throws SQLException {
        boardDao.addBoard(board);
    }

    /**
     * 모든 Board객체를 boardDAO에서 찾아서 List에 담아준 다음 반환한다
     */
    public static List<Board> getBoards() throws SQLException {
        return boardDao.getBoards();
    }

    /**
     * 제목으로 board객체를 찾음 다음 반환한다
     * @param title 찾고자 하는 board의 title
     */
    public static Board getOneBoard(String title) throws SQLException
    {
        return boardDao.getOneBoard(title);
    }
}