package http.utils;

import http.enums.HttpMethodType;

public class HttpMethodTypeUtil {
    public static HttpMethodType getHttpMethodType(String httpMethod) {
        try {
            String method = httpMethod.toUpperCase();
            return HttpMethodType.valueOf(method);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid http method: " + httpMethod);
        }
    }
}
