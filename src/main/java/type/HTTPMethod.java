package type;

public enum HTTPMethod {
    GET("GET"),
    POST("POST");

    private final String value;

    HTTPMethod(String value) { this.value = value; }

    public String getValue() { return value; }
}
