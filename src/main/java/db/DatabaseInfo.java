package db;

public enum DatabaseInfo {
    DB_URL("jdbc:h2:tcp://localhost/~/test"),
    DB_USERNAME("sa"),
    DB_PASSWORD("");

    private final String key;

    DatabaseInfo(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
