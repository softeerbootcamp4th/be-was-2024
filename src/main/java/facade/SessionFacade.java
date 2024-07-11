package facade;

import db.SessionDatabase;
import db.SessionIdMapper;
import model.Session;
import web.HttpRequest;

import java.util.Map;

public class SessionFacade {

    private static final String SESSION_ID = "SID";

    public static Session createSession(String userId) {
        // 세션 저장소에 세션 생성
        Session session = SessionDatabase.createDefaultSession();
        // 해당 세션과 userId 매핑테이블에 저장
        SessionIdMapper.addSessionId(session.getId(), userId);
        return session;
    }

    public static boolean isAuthenticatedRequest(HttpRequest request) {
        Map<String, String> cookie = request.getCookie();
        String SID = cookie.get(SESSION_ID);

        SessionDatabase.removeExpiredSessions();

        return SessionDatabase.isValidateSession(SID);
    }

    public static void invalidateAndRemoveSession(HttpRequest request) {
        String SID = request.getCookie().get(SESSION_ID);
        SessionDatabase.invalidateSession(SID);
        SessionDatabase.removeExpiredSessions();
    }

}

