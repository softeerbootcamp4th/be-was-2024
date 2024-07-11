package db;

import model.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SessionDatabase {
    private static final Logger logger = LoggerFactory.getLogger(SessionDatabase.class);

    private static Map<String, Session> sessions = new HashMap<>();

    public static void addSession(Session session) {
        logger.info("Adding session: ("+session.getUserId()+", sessinID :" + session.getSessionId(),")");
        sessions.put(session.getSessionId(), session);
    }

    public static Optional<Session> findSessionById(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

//    public static Collection<User> findAll() {
//        return users.values();
//    }
}
