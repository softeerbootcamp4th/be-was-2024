package handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler {
    private static final ConcurrentHashMap<String, User> sessionMap = new ConcurrentHashMap<>();
    //멀티스레드 환경에서 Thread-safe 한 HashMap 이다


    private static final Logger logger = LoggerFactory.getLogger(SessionHandler.class);


    public static String createSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessionMap.put(sessionId, user);
        return sessionId;
    }

    public static User getUserBySessionId(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public static void invalidateSession(String sessionId) {
        sessionMap.remove(sessionId);
    }
}