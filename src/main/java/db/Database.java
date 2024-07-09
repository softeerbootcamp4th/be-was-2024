package db;

import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Database {

    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        if(!users.containsKey(userId)){
            return Optional.empty();
        }
        return Optional.ofNullable(users.get(userId));
    }

    public static Optional<User> findUserBySessionId(String sessionId){
        for(User user : users.values()){
            if(user.getSessionId() != null && user.getSessionId().equals(sessionId)){
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public static Optional<User> login(String userId, String password) {
        if(!users.containsKey(userId)){
            return Optional.empty();
        }
        User user = users.get(userId);
        if(!user.getPassword().equals(password)){
            return Optional.empty();
        }
        return Optional.of(user);
    }

    public static void logout(String sessionId){
        for(User user : users.values()){
            if(user.getSessionId().equals(sessionId)){
                user.setSessionId(null);
                break;
            }
        }
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
