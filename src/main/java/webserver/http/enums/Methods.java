package webserver.http.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/*
* enum data of methods
* */
public enum Methods {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String method;

    Methods(String method) {
        this.method = method;
    }
    private String getMethod() {
        return this.method;
    }

    //using map to find by method string
    private static final Map<String, Methods> BY_METHOD =
            Stream.of(values()).collect(Collectors.toMap(Methods::getMethod, e -> e));

    public static Methods valueOfMethod(String method) {
        return BY_METHOD.get(method);
    }

}
