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

public class SessionHandler {

    private SessionHandler() {}

    public static SessionHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final SessionHandler INSTANCE = new SessionHandler();
    }

    public Optional<Session> login(Map<String, String> fields) {
        User user = Database.login(fields.get(ConstantUtil.USER_ID), fields.get(ConstantUtil.PASSWORD)).orElse(null);
        if (user == null) {
            return Optional.empty();
        }

        String sessionId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        Session session = new Session(sessionId, user.getUserId(), LocalDateTime.now(ZoneId.of("GMT")));
        SessionDatabase.addSession(session);
        return Optional.of(session);
    }

    public void logout(String sessionId) {
        SessionDatabase.removeSession(sessionId);
    }

    public Optional<String> parseSessionId(String cookie) {
        if (cookie == null || cookie.isBlank()) {
            return Optional.empty();
        }

        String[] cookies = cookie.split(ConstantUtil.SEMICOLON_WITH_SPACES); // ;로 시작되는 1개 이상의 공백문자 기준으로 split
        for (String c : cookies) {
            if (c.contains(ConstantUtil.SESSION_ID)) {
                int idx = c.indexOf(ConstantUtil.EQUAL);
                if(idx != -1){
                    return Optional.of(c.substring(idx + 1));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Session> findSessionById(String sessionId) {
        return SessionDatabase.findSessionById(sessionId);
    }

    public boolean validateSession(Session session) {
        if(session == null || session.isExpired()){
            return false;
        }

        session.updateSession();
        return true;
    }
}
