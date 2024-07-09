package handler;

import db.Database;
import model.User;

import java.util.Map;
import java.util.Optional;

public class UserHandler implements ModelHandler<User>{

    private UserHandler() {}

    public static UserHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UserHandler INSTANCE = new UserHandler();
    }

    public Optional<User> login(Map<String, String> fields) {
        return Optional.ofNullable(Database.login(fields.get("userId"), fields.get("password")));
    }

    @Override
    public User create(Map<String, String> fields) {
        User user = User.from(fields);
        Database.addUser(user);
        return user;
    }

    // 추후 update, read, delete 추가 가능
}
