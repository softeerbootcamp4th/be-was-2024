package facade;

import db.SessionDatabase;
import db.SessionH2Database;
import db.SessionIdMapper;
import model.Session;
import web.HttpRequest;

/**
 * 세션을 관리하는 퍼사드
 */
public class SessionFacade {

    private static final String SESSION_ID = "SID";

    /**
     * DB에서 새로운 생성을 생성하고, SessionId와 User정보를 in-memory 매퍼에 매핑시킨다
     * @param userId 세션 생성 대상 유저
     * @return 생성된 세션
     */
    public static Session createSession(String userId) {
        // 세션 저장소에 세션 생성
        Session session = SessionDatabase.createDefaultSession();
        // 해당 세션과 userId 매핑테이블에 저장
        SessionIdMapper.addSessionId(session.getId(), userId);
        return session;
    }

    /**
     * 세션 정보를 조회하여 유효한 세션인지 확인
     * @param request HTTP 요청
     * @return return if Session is validated
     */
    public static boolean isAuthenticatedRequest(HttpRequest request) {
        String SID = request.getCookie().get(SESSION_ID);

        SessionDatabase.removeExpiredSessions();

        return SessionDatabase.isValidatedSession(SID);
    }

    /**
     * 요청의 세션ID 정보를 무효화한다.
     * @param request HTTP 요청
     */
    public static void invalidateAndRemoveSession(HttpRequest request) {
        String SID = request.getCookie().get(SESSION_ID);
        SessionDatabase.invalidateSession(SID);
        SessionDatabase.removeExpiredSessions();
    }

    /**
     * 세션에서 유저 정보 추출
     * @param request HTTP 요청
     * @return 찾은 User Id
     */
    public static String getUserIdFromSession(HttpRequest request) {
        String SID = request.getCookie().get(SESSION_ID);
        return SessionIdMapper.findUserIdBySessionId(SID);
    }

}

