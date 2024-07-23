package db;

import common.StringUtils;
import model.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sessionId-Session객체 쌍을 저장하는 In-memory Database
 */
public class SessionDatabase {
    private static Map<String, Session> sessions = new ConcurrentHashMap<>();

    /**
     * UUID로 세션 ID를 생성하고, 유효시간을 30분으로 하는 세션을 생성, 저장합니다.
     * @return 생성된 세션
     */
    public static Session createDefaultSession() {
        String SID = StringUtils.createRandomUUID();
        Session createSession = new Session(SID, System.currentTimeMillis()+1800*1000);
        sessions.put(SID, createSession);
        return createSession;
    }

    /**
     * SID로 세션 정보를 조회할 수 있습니다.
     * @param SID 조회할 세션의 SID
     * @return 조회된 세션 객체
     */
    public static Session findSessionById(String SID) {
        return sessions.get(SID);
    }

    /**
     * HashMap에서 유효기간이 끝난 세션들을 찾고, 삭제합니다.
     */
    public static void removeExpiredSessions() {
        for (String id : sessions.keySet()) {
            if(sessions.get(id).getAge()<System.currentTimeMillis()) {
                sessions.remove(id);
            }
        }
    }

    /**
     * 특정 세션을 만료시킵니다.
     * @param SID 만료시킬 세션 ID
     */
    public static void invalidateSession(String SID) {
        Session session = sessions.get(SID);
        if(session!=null)
            session.invalidateSession();
    }

    /**
     * 유요한 세션 ID인지 여부를 확인합니다
     */
    public static boolean isValidatedSession(String SID) {
        if(SID==null) return false;
        else return sessions.get(SID)!=null;
    }
}
