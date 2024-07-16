package db;

import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserDatabase {
    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static User addUser(User user) {
        users.put(user.getUserId(), user);
        return users.get(user.getUserId());
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static void initialize() {
        users = new ConcurrentHashMap<>();
    }
}
