package model;

import db.Database;

import java.util.Map;

public class UserCreate {

    public static synchronized void createUser(String urlPath) {
        Map<String, String> userInfo = UserInfoExtract.extractUserInfoFromBody(urlPath);
        String userId = userInfo.get("userId");
        String password = userInfo.get("password");
        String name = userInfo.get("name");
        String email = userInfo.get("email");

        if (userId != null && password != null && name != null && email != null) {
            Database.addUser(new User(userId, password, name, email));
        }
    }

}
