package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        logger.info("Adding user: " + user.getName());
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
