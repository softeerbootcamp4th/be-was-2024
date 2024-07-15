package model;

import exception.InvalidHttpRequestException;

import java.util.Map;
import java.util.stream.Stream;

public class User implements MyTagDomain{
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
        this(
                getValueFromMap(map, "userId"),
                getValueFromMap(map, "password"),
                getValueFromMap(map, "name"),
                getValueFromMap(map, "email")
        );
    }

    private static String getValueFromMap(Map<String, String> map, String key) {
        String value = map.get(key);
        if (value == null || value.isEmpty()) {
            throw new InvalidHttpRequestException("User information cannot be null");
        }
        return value;
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
