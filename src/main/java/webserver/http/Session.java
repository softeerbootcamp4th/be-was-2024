package webserver.http;

import model.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

    private static Map<String, User> storage = new ConcurrentHashMap<>();

    public static String save(User user){
        String key = keyGen();
        storage.put(key, user);
        return key;
    }

    private static String keyGen(){
        return UUID.randomUUID().toString();
    }

    public static User get(String sessionId){
        return storage.get(sessionId);
    }

    public static void deleteAll(){
        storage.clear();
    }

    public static boolean isExist(String sessionId){
        return storage.containsKey(sessionId);
    }

    public static void delete(String sessionId){
        storage.remove(sessionId);
    }

}
