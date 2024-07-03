package db;

import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Database instance;
    private static Map<String, User> users = new HashMap<>();

    private Database() {
    }

    public static Database getInstance(){
        if(users == null) instance = new Database();
        return instance;
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
