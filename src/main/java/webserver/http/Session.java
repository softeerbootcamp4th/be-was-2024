package webserver.http;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {

    private static Map<String, User> storage = new HashMap<>();

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


}
