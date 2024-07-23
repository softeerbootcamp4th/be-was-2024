package db;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionTable {
    private static Map<UUID, Integer> sessions = new HashMap<>();

    public static synchronized void addSession(UUID sessionId, int userId) {
        sessions.put(sessionId, userId);
    }

    public static Integer findUserIdBySessionId(UUID sessionId) {
        return sessions.get(sessionId);
    }

    public static void removeSession(UUID sessionId) {
        sessions.remove(sessionId);
    }
}
