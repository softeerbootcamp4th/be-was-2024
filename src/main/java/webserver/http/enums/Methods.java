package webserver.http.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * http request의 method
 */
public enum Methods {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String method;

    Methods(String method) {
        this.method = method;
    }
    public String getMethod() {
        return this.method;
    }

    /**
     * key가 method string, value가 method enum인 map 생성
     */
    private static final Map<String, Methods> BY_METHOD =
            Stream.of(values()).collect(Collectors.toMap(Methods::getMethod, e -> e));

    /**
     * http request의 method enum을 method string으로 구한다
     */
    public static Methods valueOfMethod(String method) {
        return BY_METHOD.get(method.toUpperCase());
    }

}
