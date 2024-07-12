package model;

import db.Database;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public static void createUser(String urlPath) {
        Map<String, String> userInfo = extractUserInfoFromUrl(urlPath);
        String userId = userInfo.get("userId");
        String password = userInfo.get("password");
        String name = userInfo.get("name");
        String email = userInfo.get("email");

        if (userId != null && password != null && name != null && email != null) {
            Database.addUser(new User(userId, password, name, email));
        }
    }

    public static Map<String, String> extractUserInfoFromUrl(String urlPath) {
        String[] params = urlPath.split("&");
        Map<String, String> userInfo = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            userInfo.put(keyValue[0], keyValue[1]);
        }
        return userInfo;
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
