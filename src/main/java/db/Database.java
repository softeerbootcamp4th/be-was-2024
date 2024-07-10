package db;

import model.Session;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Database {

    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Session> sessions = new HashMap<>();

    public static void addSession(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    public static Optional<Session> findSessionById(String sessionId) {
        if(!sessions.containsKey(sessionId)){
            return Optional.empty();
        }
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        if(!users.containsKey(userId)){
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(userId));
    }

    public static Optional<User> login(String userId, String password) {
        if(!users.containsKey(userId)){
            return Optional.empty();
        }
        User user = users.get(userId);
        if(!user.getPassword().equals(password)){
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
