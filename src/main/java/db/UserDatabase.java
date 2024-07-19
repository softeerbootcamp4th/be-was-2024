package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserDatabase {
    private static final Logger logger = LoggerFactory.getLogger(UserDatabase.class);



    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        logger.info("Adding user: " + user.getName());
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
