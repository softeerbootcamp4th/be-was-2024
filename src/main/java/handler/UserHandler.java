package handler;

import db.Database;
import exception.ModelException;
import model.User;
import util.ConstantUtil;

import java.util.Collection;
import java.util.Map;

public class UserHandler implements ModelHandler<User>{

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
        return user;
    }

    @Override
    public User findById(String userId) {
        return Database.findUserById(userId).orElse(null);
    }

    @Override
    public Collection<User> findAll() {
        return Database.findAll();
    }
}
