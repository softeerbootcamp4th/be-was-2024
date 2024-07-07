package webserver.http.request;

import java.net.http.HttpResponse;

public enum Method {

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

    Method(String methodName){
        this.methodName = methodName;
    }

    public String getMethodName(){
        return methodName;
    }

    public static Method fromMethodName(String methodName) {
        for (Method method : Method.values()) {
            if (method.getMethodName().equalsIgnoreCase(methodName)) {
                return method;
            }
        }

        throw new IllegalArgumentException("No enum constant with dayName " + methodName);
    }

}
