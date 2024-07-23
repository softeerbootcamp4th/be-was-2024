package db;

import model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SessionDB implements Database<Session,String>{

    private static final Logger logger = LoggerFactory.getLogger(SessionDB.class);
    private static final SessionDB instance = new SessionDB();
    private static final Map<String, Session> sessions = new HashMap<>();

    private SessionDB() {}

    // 싱글턴 인스턴스를 반환하는 메서드
    public static SessionDB getInstance() {
        return instance;
    }

    @Override
    public Optional<Session> findById(String id) {
        return Optional.ofNullable(sessions.get(id));
    }

    @Override
    public List<Session> findAll() {
        return sessions.values().stream().toList();
    }

    @Override
    public void save(Session session) {
        logger.info("Adding session: ("+session.getUserId()+", sessionID :" + session.getSessionId(),")");
        sessions.put(session.getSessionId(), session);
    }

    @Override
    public void delete(Session session) {
        sessions.remove(session.getSessionId());

    }



}
