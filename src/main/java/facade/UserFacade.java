package facade;

import db.UserH2Database;
import model.User;

import java.util.Map;

public class UserFacade {

    public static User createUser(User user) {
        return UserH2Database.addUser(new User(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
    }

    public static User findUserByUserId(String userId) {
        return UserH2Database.findUserById(userId);
    }

    public static boolean isUserExist(Map<String, String> map) {
        User findUser = UserH2Database.findUserById(map.get("userId"));
        return findUser!=null && findUser.getPassword().equals(map.get("password"));
    }
}
