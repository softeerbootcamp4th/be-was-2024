package model;

import exception.ModelException;
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
        if (params.size() != 4 || params.values().stream().anyMatch(String::isBlank)) {
            throw new ModelException(ConstantUtil.INVALID_LOGIN);
        }

        String userId = params.get("userId");
        String password = params.get("password");
        String name = params.get("name");
        String email = params.get("email");
        if(userId == null || password == null || name == null || email == null){
            throw new ModelException(ConstantUtil.INVALID_LOGIN);
        }
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
