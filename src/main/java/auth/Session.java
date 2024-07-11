package auth;

import model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
  public static final String SESSION_ID = "sessionId";
  private final Map<String, User> store = new ConcurrentHashMap<>();
  private Session() {}

  private static class LazyHolder {
    private static final Session instance = new Session();
  }

  public static Session getInstance() {
    return LazyHolder.instance;
  }

  public User get(String sessionId) {
    return store.get(sessionId);
  }

  public void insert(String sessionId, User user) {
      store.put(sessionId, user);
  }

  public void remove(String sessionId) {
      store.remove(sessionId);
  }

  public void clear() {
      store.clear();
  }
}
