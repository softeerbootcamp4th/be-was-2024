package handler;

import db.Database;
import model.User;

import java.util.Map;
import java.util.Optional;

public class AuthHandler {

    private AuthHandler() {}

    public static AuthHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final AuthHandler INSTANCE = new AuthHandler();
    }

    public Optional<User> login(Map<String, String> fields) {
        return Database.login(fields.get("userId"), fields.get("password"));
    }

    public Optional<User> findUserBySessionId(String sessionId) {
        return Database.findUserBySessionId(sessionId);
    }

    public void logout(String sessionId) {
        if(sessionId == null) return;
        Database.logout(sessionId);
    }
}
