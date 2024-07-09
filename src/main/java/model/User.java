package model;

import java.util.Map;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(Map<String, String> map) {
        if (map.get("userId") == null || map.get("password") == null || map.get("name") == null || map.get("email") == null) {
            throw new IllegalArgumentException("User information cannot be null");
        }

        this.userId = map.get("userId");
        this.password = map.get("password");
        this.name = map.get("name");
        this.email = map.get("email");
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
