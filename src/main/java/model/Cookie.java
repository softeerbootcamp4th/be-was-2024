package model;

import java.util.UUID;

public class Cookie {
    private final String name;
    private final String id;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Cookie(String name, String id) {
        this.name = name;
        this.id = id;
    }
}
