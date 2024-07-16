package webserver.session;

import model.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static Map<String, User> sessions = new ConcurrentHashMap<>();

    //session 생성 동시성 처리
    public static synchronized String createSession(User user){
        String sessionId =
                String.valueOf(System.currentTimeMillis()).substring(8, 13)
                        + UUID.randomUUID().toString().substring(1,10);
        while(sessions.containsKey(sessionId)){
            sessionId =
                    String.valueOf(System.currentTimeMillis()).substring(8, 13)
                            + UUID.randomUUID().toString().substring(1,10);
        }
        sessions.put(sessionId, user);
        return sessionId;
    }

    //session 검색
    public static User getSession(String sessionId){
        return sessions.get(sessionId);
    }

    //session 삭제
    public static void deleteSession(String sessionId){
        sessions.remove(sessionId);
    }
}

