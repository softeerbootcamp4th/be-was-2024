package session;

import db.Database;
import db.SessionDatabase;
import model.User;
import util.ConstantUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Session 객체를 관리하는 Handler
 */
public class SessionHandler {

    private SessionHandler() {}

    public static SessionHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final SessionHandler INSTANCE = new SessionHandler();
    }

    /**
     * Login 요청 처리 후 성공 시 Session 생성
     * @param fields
     * @return Session
     */
    public Optional<Session> login(Map<String, String> fields) {
        User user = Database.login(fields.get(ConstantUtil.USER_ID), fields.get(ConstantUtil.PASSWORD)).orElse(null);
        if (user == null) {
            return Optional.empty();
        }

        String sessionId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        while(SessionDatabase.findSessionById(sessionId).isPresent()){ // 중복되는 sessionId가 없도록 while문으로 처리
            sessionId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        }
        Session session = new Session(sessionId, user.getUserId(), LocalDateTime.now(ZoneId.of(ConstantUtil.GMT)));
        SessionDatabase.addSession(session);
        return Optional.of(session);
    }

    /**
     * Logout 요청 처리 후 Session 삭제
     * @param sessionId
     */
    public void logout(String sessionId) {
        SessionDatabase.removeSession(sessionId);
    }

    /**
     * Session ID로 Session 조회
     * @param sessionId
     * @return Session
     */
    public Optional<Session> findSessionById(String sessionId) {
        return SessionDatabase.findSessionById(sessionId);
    }

    /**
     * Session 만료 여부 확인 후, 만료 시 삭제하며 그렇지 않으면 갱신
     * @param session
     * @return boolean
     */
    public boolean validateSession(Session session) {
        if(session.isExpired()){
            SessionDatabase.removeSession(session.getSessionId());
            return false;
        }

        session.updateSession();
        return true;
    }
}
