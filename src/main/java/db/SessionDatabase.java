package db;

import common.StringUtils;
import model.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sessionId-Session객체 쌍 저장
 */
public class SessionDatabase {
    private static Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static Session createDefaultSession() {
        String SID = StringUtils.createRandomUUID();
        Session createSession = new Session(SID, System.currentTimeMillis()+1800*1000);
        sessions.put(SID, createSession);
        return createSession;
    }

    public static Session findSessionById(String SID) {
        return sessions.get(SID);
    }

    public static void removeExpiredSessions() {
        for (String id : sessions.keySet()) {
            if(sessions.get(id).getAge()<System.currentTimeMillis()) {
                sessions.remove(id);
            }
        }
    }

    public static void invalidateSession(String SID) {
        Session session = sessions.get(SID);
        if(session!=null)
            session.invalidateSession();
    }

    public static boolean isValidatedSession(String SID) {
        return sessions.get(SID)!=null;
    }
}
