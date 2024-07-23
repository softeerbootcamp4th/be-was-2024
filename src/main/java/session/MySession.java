package session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 세션 저장소
 */
public class MySession {
    private static final Map<String, Object> sessionStore = new HashMap<>();

    private static String getRandomUUIDString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static synchronized String createSession(Object value) {
        String sessionKey;
        do {
            sessionKey = getRandomUUIDString();
        } while (sessionStore.containsKey(sessionKey));

        sessionStore.put(sessionKey, value);
        return sessionKey;
    }

    public static Object getSession(String key) {
        return sessionStore.get(key);
    }

    public static synchronized void clearSession(String key) {
        sessionStore.remove(key);
    }
}
