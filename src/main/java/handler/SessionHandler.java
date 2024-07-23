package handler;

import model.User;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션을 생성하고 보관하는 클래스
 */
public class SessionHandler {
    private static final ConcurrentHashMap<String, User> sessionMap = new ConcurrentHashMap<>();
    //멀티스레드 환경에서 Thread-safe 한 HashMap 이다



    /**
     * 세션을 만드는 클래스
     * @param user 세션값을 배정하고자하는 User객체
     */
    synchronized//만에 하나에 세션이 중복될 상황을 대비해서
    public static String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessionMap.put(sessionId, user);
        return sessionId;
    }

    /**
     * sessionId로 User를 찾고 해당 User객체를 반환하는 클래스
     * @param sessionId 찾고자하는 User객체의 sessionId 값
     */
    public static User getUserBySessionId(String sessionId) {
        return sessionMap.get(sessionId);
    }


    /**
     * 해당하는 세션을 만료시키는 클래스
     * @param sessionId 만료시키고자 하는 세션 Id
     */
    public static void invalidateSession(String sessionId) {
        sessionMap.remove(sessionId);
    }//실행시켜줘야돼요
}