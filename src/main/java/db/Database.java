package db;

import model.User;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    static{
        addUser(new User("a", "a", "a", "a"));
    }
    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
