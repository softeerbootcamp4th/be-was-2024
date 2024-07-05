package handler;

import db.Database;
import model.User;

import java.util.Map;

public class UserHandler implements ModelHandler<User> {

    private UserHandler() {}

    public static UserHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UserHandler INSTANCE = new UserHandler();
    }

    @Override
    public User create(Map<String, String> fields) {
        User user = User.from(fields);
        Database.addUser(user);
        System.out.println("New " + user);
        return user;
    }

    // 추후 update, read, delete 추가 가능
}
