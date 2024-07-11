package db;

import model.Session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {
    private static final Map<Long, Session> sessions = new ConcurrentHashMap<>();

    public static void addSession(Long sessionId, Session session) {
        sessions.put(sessionId, session);
    }

    public static Optional<Session> getSession(Long sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public static void removeSession(Long sessionId) {
        sessions.remove(sessionId);
    }
}
