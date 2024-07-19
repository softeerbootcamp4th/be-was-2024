package webserver.http;

import model.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션에 대한 정보를 담는 클래스
 */
public class Session {

    private static Map<String, User> storage = new ConcurrentHashMap<>();

    /**
     * 유저 정보를 세션에 저장하는 메소드
     * @param user
     * @return
     */
    public static String save(User user){
        String key = keyGen();
        storage.put(key, user);
        return key;
    }

    private static String keyGen(){
        return UUID.randomUUID().toString();
    }

    /**
     * 세션 id 에 대한 유저를 반환하는 메소드
     * @return
     */
    public static User get(String sessionId){
        return storage.get(sessionId);
    }

    /**
     * 세션 id 에 대한 유저가 존재하는지 반환하는 메소드
     * @param sessionId
     * @return
     */
    public static boolean isExist(String sessionId){
        return storage.containsKey(sessionId);
    }

    /**
     * 세션 id 에 대한 유저 정보를 제거하는 메소드
     * @param sessionId
     */
    public static void delete(String sessionId){
        storage.remove(sessionId);
    }

}
