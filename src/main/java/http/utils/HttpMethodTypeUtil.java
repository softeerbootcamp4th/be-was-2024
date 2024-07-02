package http.utils;

import http.HttpMethodType;

public class HttpMethodTypeUtil {
    public static HttpMethodType getHttpMethodType(String httpMethod) {
        String method = httpMethod.toUpperCase();
        return switch (method) {
            case "GET" -> HttpMethodType.GET;
            case "POST" -> HttpMethodType.POST;
            case "PUT" -> HttpMethodType.PUT;
            case "DELETE" -> HttpMethodType.DELETE;
            case "PATCH" -> HttpMethodType.PATCH;
            default -> throw new RuntimeException("unsupported http method: " + httpMethod);
        };
    }
}
