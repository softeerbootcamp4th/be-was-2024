package model;

import db.Session;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * SessionId 생성과 삭제를 담당하는 클래스
 */
public class SessionIdControl {

    private static SecureRandom random = new SecureRandom();

    /**
     * 랜덤으로 생성된 SessionId 정보를 생성하는 메서드
     * @param user SessionId를 생성하고자 하는 유저 객체
     * @return 생성된 SessionId 정보
     */
    public static synchronized String createSessionId(User user) {
        String sessionId = new BigInteger(130, random).toString(32);
        User existUser = Session.findUserBySessionId(sessionId);
        while(existUser != null){
            sessionId = new BigInteger(130, random).toString(32);
            existUser = Session.findUserBySessionId(sessionId);
        }
        Session.addSessionId(sessionId, user);
        return sessionId;
    }

    /**
     * 저장된 SessionId 정보를 삭제하는 메서드
     * @param sessionId 지우고자 하는  SessionId 정보
     */
    public static synchronized void deleteSessionId(String sessionId){
        Session.deleteSessionId(sessionId);
    }
}
