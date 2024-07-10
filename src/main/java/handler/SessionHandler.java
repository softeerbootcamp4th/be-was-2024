package handler;

import model.User;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionHandler {
    private static ConcurrentHashMap<String, User> sessionIdList = new ConcurrentHashMap<>();

    public static void makeNewSessionId(User user) {
        // 랜덤한 session id 생성
        String sessionId = UUID.randomUUID().toString();
        sessionIdList.put(sessionId, user);
    }

    public static String getSessionId(String userId) {
        // userId를 이용해 session id 알아내기
        for (String sessionId : sessionIdList.keySet()) {
            if (sessionIdList.get(sessionId).getUserId().equals(userId)) {
                return sessionId;
            }
        }
        return null;
    }

    public static User getUser(String sessionId) {
        // session id를 이용해 user 알아내기
        return sessionIdList.get(sessionId);
    }
}
