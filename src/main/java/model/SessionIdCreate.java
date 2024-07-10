package model;

import db.Session;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SessionIdCreate {

    private static SecureRandom random = new SecureRandom();

    public static String nextSessionId(User user) {
        String sessionId = new BigInteger(130, random).toString(32);
        User existUser = Session.findUserBySessionId(sessionId);
        while(existUser != null){
            sessionId = new BigInteger(130, random).toString(32);
            existUser = Session.findUserBySessionId(sessionId);
        }
        Session.addSessionId(sessionId, user);
        return sessionId;
    }
}
