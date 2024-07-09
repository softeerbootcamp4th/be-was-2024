package db;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class Session {
    private static Map<String, User> sessionMap = new ConcurrentHashMap<>();

    public static void addNewSession(String sessionId,User user) {
        sessionMap.put(sessionId, user);
    }

    public static User getUser(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public static void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }
}
