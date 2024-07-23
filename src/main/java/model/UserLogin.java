package model;

import db.Database;

/**
 * 유저의 로그인 처리를 담당하는 클래스
 */
public class UserLogin {

    /**
     * 유저의 정보 비교를 통한 로그인 처리를 담당하는 메서드
     * @param body 유저의 정보를 담고 있는 변수
     * @return 로그인 성공한 유저의 객체
     */
    public static User login(String body) {
        User user = UserInfoExtract.extractUserInfoFromBodyForLogin(body);
        if(Database.findUserById(user.getUserId()) == null){
            return null;
        }

        User existUser = Database.findUserById(user.getUserId());
        if(!existUser.getPassword().equals(user.getPassword())){
            return null;
        }
        return existUser;
    }
}
