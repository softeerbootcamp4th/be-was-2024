package facade;

import db.UserDatabase;
import model.User;

import java.util.Map;

public class UserFacade {

    public static User createUser(User user) {
        return UserDatabase.addUser(new User(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
    }

    public static User findUserByUserId(String userId) {
        return UserDatabase.findUserById(userId);
    }

    public static boolean isUserExist(Map<String, String> map) {
        User findUser = UserDatabase.findUserById(map.get("userId"));
        return findUser!=null && findUser.getPassword().equals(map.get("password"));
    }
}
