package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

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
