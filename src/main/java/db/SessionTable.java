package db;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionTable {
    private static Map<UUID, String> sessions = new HashMap<>();

    public static void addSession(UUID sessionId, String userId) {
        sessions.put(sessionId, userId);
    }

    public static String findUserIdBySessionId(UUID sessionId) {
        return sessions.get(sessionId);
    }

    public static void removeSession(UUID sessionId) {
        sessions.remove(sessionId);
    }
}
