package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDB implements StringIdDatabase<User>
{
    private static final Logger logger = LoggerFactory.getLogger(UserDB.class);
    private static final UserDB instance = new UserDB();
    private static final Map<String, User> users = new HashMap<>();

    private UserDB() {}

    // 싱글턴 인스턴스를 반환하는 메서드
    public static UserDB getInstance() {
        return instance;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public void save(User user) {
        logger.info("Adding user: " + user.getName());
        users.put(user.getUserId(), user);
    }

    @Override
    public void delete(User user) {

    }
}
