package db;

import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        if(!users.containsKey(userId)){
            return null;
        }
        return users.get(userId);
    }

    public static User login(String userId, String password) {
        if(!users.containsKey(userId)){
            return null;
        }
        User user = users.get(userId);
        if(!user.getPassword().equals(password)){
            return null;
        }
        return user;
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
