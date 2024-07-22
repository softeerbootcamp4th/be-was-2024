package utils;

public class Cookie {
    private final String name;
    private final String value;
    private final String path;
    private final int maxAge;

    // Private constructor to enforce the use of the builder
    private Cookie(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
        this.path = builder.path;
        this.maxAge = builder.maxAge;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }

    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public String toString() {
        StringBuilder header = new StringBuilder();
        header.append(name).append("=").append(value);

        if (path != null) {
            header.append("; Path=").append(path);
        }

        if (maxAge >= 0) {
            header.append("; Max-Age=").append(maxAge);
        }

        return header.toString();
    }

    public static class Builder {
        private final String name;
        private final String value;
        private String path = "/";
        private int maxAge = -1;

        public Builder(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder maxAge(int maxAge) {
            this.maxAge = maxAge;
            return this;
        }

        public Cookie build() {
            return new Cookie(this);
        }
    }
}
