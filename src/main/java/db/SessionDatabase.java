package db;

import session.Session;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session을 비즈니스 로직과 분리하기 위한 별도의 Database
 */
public class SessionDatabase {

    private SessionDatabase() {
    }

    private static Map<String, Session> sessions = new ConcurrentHashMap<>();

    /**
     * Session을 추가
     * @param session
     */
    public static void addSession(Session session) {
        sessions.put(session.getSessionId(), session);
    }

    /**
     * Session ID로 Session을 조회
     * @param sessionId
     * @return Session
     */
    public static Optional<Session> findSessionById(String sessionId) {
        if(!sessions.containsKey(sessionId)){
            return Optional.empty();
        }
        return Optional.ofNullable(sessions.get(sessionId));
    }

    /**
     * Session ID로 Session 삭제
     * @param sessionId
     */
    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }
}
