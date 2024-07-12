package model;

import java.util.HashMap;

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

    public static User from(HashMap<String, String> data) {
        return new User(data.get("userId"), data.get("password"), data.get("name"), data.get("email"));
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
