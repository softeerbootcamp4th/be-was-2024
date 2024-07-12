package handler;

import db.Database;
import exception.ModelException;
import model.User;
import util.ConstantUtil;

import java.util.List;
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

    @Override
    public Optional<User> create(Map<String, String> fields) {
        if (fields.size() != 4 || fields.values().stream().anyMatch(String::isBlank)) {
            throw new ModelException(ConstantUtil.INVALID_SIGNUP);
        }
        if(!fields.get(ConstantUtil.EMAIL).matches(ConstantUtil.EMAIL_REGEX)){
            throw new ModelException(ConstantUtil.INVALID_SIGNUP);
        }

        User user = User.from(fields);
        Database.addUser(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> findById(String userId) {
        return Database.findUserById(userId);
    }

    @Override
    public List<User> findAll() {
        return Database.findAll().stream().toList();
    }
}
