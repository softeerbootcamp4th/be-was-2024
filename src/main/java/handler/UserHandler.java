package handler;

import db.Database;
import exception.ModelException;
import model.User;
import util.ConstantUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * User 객체를 관리하는 Handler
 */
public class UserHandler implements ModelHandler<User>{

    private UserHandler() {}

    public static UserHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UserHandler INSTANCE = new UserHandler();
    }

    /**
     * 매개변수 검증 후 회원가입(User 객체 생성)
     * @param fields
     * @return User
     */
    @Override
    public Optional<User> create(Map<String, String> fields) {
        if (fields.size() != 4 || fields.values().stream().anyMatch(String::isBlank)) {
            throw new ModelException(ConstantUtil.INVALID_SIGNUP);
        }
        if(!fields.get(ConstantUtil.EMAIL).matches(ConstantUtil.EMAIL_REGEX)){
            throw new ModelException(ConstantUtil.INVALID_SIGNUP);
        }
        if(fields.get(ConstantUtil.USER_ID) == null || fields.get(ConstantUtil.PASSWORD) == null || fields.get(ConstantUtil.NAME) == null || fields.get(ConstantUtil.EMAIL) == null){
            throw new ModelException(ConstantUtil.INVALID_SIGNUP);
        }

        User user = User.from(fields);
        Database.addUser(user);
        return Optional.of(user);
    }

    /**
     * User 객체 조회
     * @param userId
     * @return User
     */
    @Override
    public Optional<User> findById(String userId) {
        return Database.findUserById(userId);
    }

    /**
     * 모든 User 객체 조회
     * @return List
     */
    @Override
    public List<User> findAll() {
        return Database.findAllUser().stream().toList();
    }
}
