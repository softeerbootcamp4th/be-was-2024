package db;

import session.Session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {

    private static Map<String, Session> sessions = new ConcurrentHashMap<>();

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
}
