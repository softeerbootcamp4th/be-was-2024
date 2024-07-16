package model;

import db.Database;

public class UserLogin {

    public static User login(String body) {
        User user = UserInfoExtract.extractUserInfoFromBodyForLogin(body);
        if(Database.findUserById(user.getUserId()) == null){
            return null;
        }

        User existUser = Database.findUserById(user.getUserId());
        if(!existUser.getPassword().equals(user.getPassword())){
            return null;
        }
        return existUser;
    }
}
