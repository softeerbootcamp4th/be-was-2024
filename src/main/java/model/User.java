package model;

import util.ConstantUtil;

import java.util.Map;

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

    public static User from(Map<String, String> params){
        String userId = params.get(ConstantUtil.USER_ID);
        String password = params.get(ConstantUtil.PASSWORD);
        String name = params.get(ConstantUtil.NAME);
        String email = params.get(ConstantUtil.EMAIL);
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
