package model;

public class Session {
    private final String id;
    private long age;

    public String getId() {
        return id;
    }

    public long getAge() {
        return age;
    }

    public Session(String id, long age) {
        this.id = id;
        this.age = age;
    }

    public void invalidateSession() {
        this.age = -1L;
    }
}
