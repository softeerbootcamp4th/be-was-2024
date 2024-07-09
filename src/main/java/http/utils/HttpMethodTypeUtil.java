package http.utils;

import http.enums.HttpMethodType;

public class HttpMethodTypeUtil {
    public static HttpMethodType getHttpMethodType(String httpMethod) {
        String method = httpMethod.toUpperCase();
        try {
            return HttpMethodType.valueOf(method);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid http method: " + httpMethod);
        }
    }
}
