package model;

import exception.InvalidHttpRequestException;

import java.util.Map;
import java.util.stream.Stream;

public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(Map<String, String> map) {
        String[] keys = {"userId", "password", "name", "email"};
        if(Stream.of(keys).anyMatch(key -> map.get(key) == null || map.get(key).isEmpty()))
            throw new InvalidHttpRequestException("User information cannot be null");

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
