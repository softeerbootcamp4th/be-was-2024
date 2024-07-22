package db;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 ID와 유저 ID를 매핑해주는 매퍼 클래스입니다.
 */
public class SessionIdMapper {
    private static Map<String, String> sessionIds = new ConcurrentHashMap<>();

    /**
     * @return userId
     */
    public static String addSessionId(String sessionId, String userId) {
        return sessionIds.put(sessionId, userId);
    }

    /**
     * @return userId
     */
    public static String findUserIdBySessionId(String sessionId) {
        return sessionIds.get(sessionId);
    }

    public static void initialize() {
        sessionIds = new ConcurrentHashMap<>();
    }


}
