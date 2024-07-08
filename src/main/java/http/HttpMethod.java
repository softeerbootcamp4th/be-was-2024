package http;

public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    public static HttpMethod getMethod(String method) {
        switch (method) {
            case "GET" -> {
                return GET;
            }
            case "POST" -> {
                return POST;
            }
            case "PUT" -> {
                return PUT;
            }
            case "PATCH" -> {
                return PATCH;
            }
            case "DELETE" -> {
                return DELETE;
            }
            default -> throw new IllegalArgumentException("Unknown HTTP method: " + method);
        }
    }
}
