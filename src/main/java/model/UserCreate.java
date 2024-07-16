package model;

import db.Database;

public class UserCreate {

    public static synchronized void createUser(String urlPath) {
        Database.addUser(UserInfoExtract.extractUserInfoFromBodyForCreate(urlPath));
    }

}
