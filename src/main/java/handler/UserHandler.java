package handler;

import db.Database;
import model.User;

import java.util.Map;

public class UserHandler {

    private static UserHandler instance = null;

    public static UserHandler getInstance() {
        if (instance == null) {
            instance = new UserHandler();
        }
        return instance;
    }

    public void createUser(Map<String, String> params) {
        User user = User.from(params);
        Database.addUser(user);
        System.out.println("New " + user);
    }

    // 추후 update, read, delete 추가 가능
}
