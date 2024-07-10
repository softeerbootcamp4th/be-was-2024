package webserver;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static final Map<String, User> sessionMap = new HashMap<>();

    public static String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessionMap.put(sessionId, user);
        return sessionId;
    }

    public static User getUser(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public static boolean isValidSession(String sessionId) {
        return sessionMap.containsKey(sessionId);
    }

    public static void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }
}
