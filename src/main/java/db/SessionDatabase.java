package db;

import model.Session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static void addSession(String sessionId, Session session) {
        sessions.put(sessionId, session);
    }

    public static Optional<Session> getSession(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
