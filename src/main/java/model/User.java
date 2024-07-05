package model;

import static db.Database.addUser;

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

    public static User createUserFromUrl(String urlPath) {
        int parameterIndex = urlPath.indexOf("?");
        if (parameterIndex != -1) {
            String[] userInfo = urlPath.substring(parameterIndex + 1).split("&");
            return new User(
                    userInfo[0].substring(userInfo[0].indexOf("=") + 1),
                    userInfo[1].substring(userInfo[1].indexOf("=") + 1),
                    userInfo[2].substring(userInfo[2].indexOf("=") + 1),
                    userInfo[3].substring(userInfo[3].indexOf("=") + 1)
            );
        }
        return null;
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
