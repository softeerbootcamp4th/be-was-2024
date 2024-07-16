package db;

import model.Board;
import model.User;

import java.util.*;

public class Database {
    private static Map<String, User> users = new HashMap<>();

    private static List<Board> boards = new ArrayList<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId)
    {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }


    public static void addBoard(Board board)
    {
        boards.add(board);
    }

    public static List<Board> getBoards(){
        return boards;
    }

}
