package enums;

public enum Method {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE;

    public static Method fromString(String requestMethod) {
        for (Method method : Method.values()) {
            if (method.name().equalsIgnoreCase(requestMethod)) {
                return method;
            }
        }

        throw new IllegalArgumentException("Unknown HTTP method: " + requestMethod);
    }
}
