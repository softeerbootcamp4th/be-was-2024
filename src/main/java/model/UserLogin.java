package model;

import db.Database;

import java.util.Map;

public class UserLogin {

    public static User login(String body) {
        Map<String, String> userInfo = UserInfoExtract.extractUserInfoFromBody(body);
        if(Database.findUserById(userInfo.get("userId")) == null){
            return null;
        }

        User user = Database.findUserById(userInfo.get("userId"));
        if(!user.getPassword().equals(userInfo.get("password"))){
            return null;
        }
        return user;
    }
}
