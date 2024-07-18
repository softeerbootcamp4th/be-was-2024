package db;

import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static final Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static boolean userExists(String userId) {
        return users.containsKey(userId);
    }

    public static Map<String, User> findAll() {
        return users;
    }

    public static List<User> findAllByList() {
        return new ArrayList<>(users.values());
    }
}
