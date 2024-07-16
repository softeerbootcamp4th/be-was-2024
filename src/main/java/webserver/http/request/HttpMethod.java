package webserver.http.request;

public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    DELETE("DELETE"),
    PUT("PUT"),
    PATCH("PATCH"),
    OPTION("OPTION"),
    HEAD("HEAD"),
    TRACE("TRACE"),
    CONNECT("CONNECT");

    private final String methodName;

    HttpMethod(String methodName){
        this.methodName = methodName;
    }

    public String getMethodName(){
        return methodName;
    }

    public static HttpMethod fromMethodName(String methodName) {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.getMethodName().equalsIgnoreCase(methodName)) {
                return httpMethod;
            }
        }

        throw new IllegalArgumentException("No enum constant with dayName " + methodName);
    }

}
