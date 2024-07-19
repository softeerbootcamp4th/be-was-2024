package facade;

import db.SessionDatabase;
import db.SessionH2Database;
import db.SessionIdMapper;
import model.Session;
import web.HttpRequest;

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
        String SID = request.getCookie().get(SESSION_ID);
        System.out.println("SID = " + SID);

        SessionDatabase.removeExpiredSessions();

        return SessionDatabase.isValidatedSession(SID);
    }

    public static void invalidateAndRemoveSession(HttpRequest request) {
        String SID = request.getCookie().get(SESSION_ID);
        SessionDatabase.invalidateSession(SID);
        SessionDatabase.removeExpiredSessions();
    }

    public static String getUserIdFromSession(HttpRequest request) {
        String SID = request.getCookie().get(SESSION_ID);
        return SessionIdMapper.findUserIdBySessionId(SID);
    }

}

