package model;

import db.Database;

/**
 * 유저의 회원가입 처리를 담당하는 클래스
 */
public class UserCreate {

    /**
     * 유저의 회원가입 처리를 담당하는 메서드
     * @param body 회원가입 하려는 정보가 담긴 변수
     */
    public static synchronized void createUser(String body) {
        Database.addUser(UserInfoExtract.extractUserInfoFromBodyForCreate(body));
    }

}
