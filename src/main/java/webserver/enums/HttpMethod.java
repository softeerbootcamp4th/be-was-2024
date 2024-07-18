package webserver.enums;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String method;

    private HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static HttpMethod of(String method) {
        for (HttpMethod httpMethod : values()) {
            if (httpMethod.getMethod().equals(method)) {
                return httpMethod;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 HttpMethod 입니다.");
    }
}
