package db;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static ConcurrentHashMap<String, String> sessionDatabase = new ConcurrentHashMap<>();

    public static String createSession(String userId){
        String sid = UUID.randomUUID().toString();
        sessionDatabase.put(sid, userId);
        return sid;
    }

    public static String getUser(String sid){
        return sessionDatabase.get(sid);
    }

    public static void deleteSession(String sid){
        sessionDatabase.remove(sid);
    }
}
