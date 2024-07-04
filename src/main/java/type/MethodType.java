package type;

public enum MethodType {
    GET("GET"),
    POST("POST");

    private final String value;

    MethodType(String value) { this.value = value; }

    public String getValue() { return value; }
}
