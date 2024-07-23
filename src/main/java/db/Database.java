package db;

import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Database {
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<String, User>();
    private static ConcurrentHashMap<String, String> sessions = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static boolean addSession(String sessionId, String userId) {
        if (sessions.contains(sessionId)) return false;
        sessions.put(sessionId, userId);
        return true;
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static String findUserIdBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }
}
