package db;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 로그인한 사용자의 세션을 저장하는 데이터베이스 클래스입니다.
 */
public class SessionDatabase {
    /**
     * 세션을 저장하는 데이터베이스입니다.
     * <pre>
     *     key: Session Id
     *     value: User Id
     * </pre>
     */
    private static final ConcurrentHashMap<String, String> sessionDatabase = new ConcurrentHashMap<>();

    /**
     * 세션을 생성하는 메서드입니다.
     * 랜덤한 SID를 발급하여 세션 데이터베이스에 등록합니다.
     * @param userId 문자열로 표현된 유저 아이디
     * @return SID를 생성하여 반환합니다.
     */
    public static String createSession(String userId){
        String sid = UUID.randomUUID().toString();
        sessionDatabase.put(sid, userId);
        return sid;
    }

    /**
     * 세션 아이디를 통해 유저 아이디를 반환하는 메서드입니다.
     * @param sid 문자열로 표현된 Session id
     * @return 문자열로 표현된 User Id를 반환합니다.
     */
    public static String getUser(String sid){
        return sessionDatabase.get(sid);
    }
    // 옵셔널 -> 가독성, 실수 줄이기, 체이닝 등 기능

    /**
     * 등록된 세션을 데이터베이스에서 삭제하는 메서드입니다.
     * @param sid 문자열로 표현된 Session Id
     */
    public static void deleteSession(String sid){
        sessionDatabase.remove(sid);
    }

    /**
     * 로그인 여부를 확인하는 메서드입니다.
     * @param sid 문자열로 표현된 Session Id
     * @return 로그인 시 true, 세션 아이디가 없거나 세션 데이터베이스에 등록되지 않은 Session Id를 보유한 사용자는 비 로그인으로 간주, false를 반환합니다.
     */
    public static Boolean isLogin(String sid){
        if(sid == null) return false;
        String userId = sessionDatabase.get(sid);
        return userId != null;
    }
}
