package handler;

import db.Database;
import model.Session;
import model.User;

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

    public Optional<String> parseSessionId(String cookie) {
        if (cookie == null) {
            return Optional.empty();
        }

        String[] cookies = cookie.split(";\\s+"); // ;로 시작되는 1개 이상의 공백문자 기준으로 split
        for (String c : cookies) {
            if (c.contains("sid")) {
                int idx = c.indexOf("=");
                if(idx != -1){
                    return Optional.of(c.substring(idx + 1));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Session> login(Map<String, String> fields) {
        User user = Database.login(fields.get("userId"), fields.get("password")).orElse(null);
        if (user == null) {
            return Optional.empty();
        }

        String sessionId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        Session session = new Session(sessionId, user.getUserId());
        Database.addSession(session);
        return Optional.of(session);
    }

    public Optional<Session> findUserBySessionId(String sessionId) {
        return Database.findSessionById(sessionId);
    }

    public void logout(String sessionId) {
        Database.removeSession(sessionId);
    }
}
