package db;

import model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static void addSessionId(String sessionId, User user) {users.put(sessionId, user);}

    public static User findUserBySessionId(String sessionId) {
        return users.get(sessionId);
    }

    public static void deleteSessionId(String sessionId){
        users.remove(sessionId);
    }
}
