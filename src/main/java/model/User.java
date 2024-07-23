package model;

import util.ConstantUtil;

import java.util.Map;

/**
 * 사용자 정보를 저장하는 클래스
 */
public class User {

    private String userId;
    private String password;
    private String name;
    private String email;

    protected User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    /**
     * 요청 Layer에서 Map을 객체로 변환하기 위해 사용
     * @param params
     * @return User
     */
    public static User from(Map<String, String> params){
        String userId = params.get(ConstantUtil.USER_ID);
        String password = params.get(ConstantUtil.PASSWORD);
        String name = params.get(ConstantUtil.NAME);
        String email = params.get(ConstantUtil.EMAIL);
        return new User(userId, password, name, email);
    }

    /**
     * DB Layer에서, 테이블을 객체로 변환하기 위해 사용
     * @param userId
     * @param password
     * @param name
     * @param email
     * @return User
     */
    public static User of(String userId, String password, String name, String email){
        return new User(userId, password, name, email);
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
