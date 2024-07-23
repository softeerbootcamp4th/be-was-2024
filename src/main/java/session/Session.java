package session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session을 관리하는 클래스
 */
public class Session {
    private static final Map<String, String> sessionDb = new ConcurrentHashMap<>();

    /**
     * 유저의 session 정보를 sessionDB에 저장한다.
     *
     * @param userID : 유저의 userId
     * @return : sessionId 반환
     */
    public static String createSession(String userID) {
        String sessionId = UUID.randomUUID().toString();
        sessionDb.put(sessionId, userID);
        return sessionId;
    }

    /**
     * sessionId를 사용하여 유저의 userId를 반환한다.
     *
     * @param sessionId : 유저의 sessionId
     * @return : 유저의 userId
     */
    public static String getUserId(String sessionId) {
        return sessionDb.get(sessionId);
    }

    /**
     * 유저의 session 정보를 sessionDB에서 삭제한다.
     *
     * @param sessionId : 유저의 sessionId
     */
    public static void deleteSession(String sessionId) {
        sessionDb.remove(sessionId);
    }

}
