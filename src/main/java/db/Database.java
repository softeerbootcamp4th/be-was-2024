package db;

import model.User;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    // 로그인 테스트용 더미데이터
    static{
        addUser(new User("test", "test", "test", "test"));
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
