package model;

import db.Database;

import java.util.HashMap;
import java.util.Map;

/**
 * body의 유저 정보 추출을 담당하는 클래스
 */
public class UserInfoExtract {

    /**
     * 회원 가입을 위한 정보 추출을 담당하는 메서드
     * @param body 유저id,password,name,email이 담긴 변수
     * @return 회원가입 요구 정보와 부합하는 유저의 객체
     */
    public static User extractUserInfoFromBodyForCreate(String body) {
        String[] params = body.split("&");
        Map<String, String> userInfo = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            userInfo.put(keyValue[0], keyValue[1]);
        }
        String userId = userInfo.get("userId");
        String password = userInfo.get("password");
        String name = userInfo.get("name");
        String email = userInfo.get("email");

        if(Database.findUserById(userId) != null){
            throw new RuntimeException();
        }
        User user = null;
        if (userId != null && password != null && name != null && email != null) {
            user = new User(userId, password, name, email);
        }
        return user;
    }

    /**
     * 로그인을 위한 정보 추출을 담당하는 메서드
     * @param body 유저id,password가 담긴 변수
     * @return 로그인 요구 정보와 부합하는 유저의 객체
     */
    public static User extractUserInfoFromBodyForLogin(String body) {
        String[] params = body.split("&");
        Map<String, String> userInfo = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            userInfo.put(keyValue[0], keyValue[1]);
        }
        String userId = userInfo.get("userId");
        String password = userInfo.get("password");

        User user = null;
        if (userId != null && password != null) {
            user = new User(userId, password);
        }
        return user;
    }

    /**
     * 세션에서의 User 정보를 추출하는 메서드
     * @param header SessionId가 담긴 헤더 정보
     * @return 추출된 SessionId
     */
    public static String extractSessionIdFromHeader(String header){
        if(header.split(";").length == 1){
            return "";
        }
        String sessionId = header.split(";")[1].substring(5);
        return sessionId;
    }
}
