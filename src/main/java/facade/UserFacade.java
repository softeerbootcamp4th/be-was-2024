package facade;

import db.Database;
import model.User;

import java.util.Map;

public class UserFacade {

    public static boolean isUserExist(Map<String, String> map) {
        User findUser = Database.findUserById(map.get("userId"));
        return findUser!=null && findUser.getPassword().equals(map.get("password"));
    }
}
