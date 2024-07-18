package auth;

public class Cookie {
  public static final String SET_COOKIE = "set-cookie";
  public static final String MAX_AGE = "maxage";
  public static final String PATH = "path";
  private String key;
  private String value;

  private int maxAge;
  private String path;

  private Cookie() {}
  public Cookie(String key, String value) {
    this.key = key;
    this.value = value;
    this.maxAge=3600;
    this.path="/";
  }

  public int getMaxAge() {
    return maxAge;
  }

  public String getPath() {
    return path;
  }

  public Cookie maxAge(int maxAge) {
    this.maxAge = maxAge;
    return this;
  }

  public Cookie path(String path) {
    this.path = path;
    return this;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}