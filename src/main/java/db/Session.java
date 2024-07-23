package db;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 인 메모리 관리 클래스
 */
public class Session {
    private static Map<String, User> sessionMap = new ConcurrentHashMap<>();

    /**
     * 새로운 유저 세션 추가
     * @param sessionId
     * @param user
     */
    public static void addNewSession(String sessionId,User user) {
        sessionMap.put(sessionId, user);
    }

    /**
     * 세션 ID로 유저 반환
     * @param sessionId
     * @return 세션 ID에 해당하는 User
     */
    public static User getUser(String sessionId) {
        return sessionMap.get(sessionId);
    }

    /**
     * 세션 ID로 세션 삭제
     * @param sessionId
     */
    public static void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }
}
