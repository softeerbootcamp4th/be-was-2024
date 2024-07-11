package session;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static final ConcurrentHashMap<String, String> sessionDb = new ConcurrentHashMap<>();

    // 세션 DB에 userId 저장
    public static String createSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        sessionDb.put(sessionId, userId);
        return sessionId;
    }

    // userId 조회
    public static String getUserId(String sessionId) {
        return sessionDb.get(sessionId);
    }

    // 세션 삭제
    public static void deleteSession(String sessionId) {
        sessionDb.remove(sessionId);
    }

}
