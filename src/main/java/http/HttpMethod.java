package http;

/**
 * HttpMethod를 표현하는 Enum 클래스입니다.
 */
public enum HttpMethod {
    GET, POST, PUT, PATCH, DELETE;

    /**
     * 문자열 method를 HttpMethod로 매핑하는 메서드입니다.
     * @param method
     * @return
     */
    public static HttpMethod getMethod(String method) {
        switch (method) {
            case "GET" -> {
                return GET;
            }
            case "POST" -> {
                return POST;
            }
            case "PUT" -> {
                return PUT;
            }
            case "PATCH" -> {
                return PATCH;
            }
            case "DELETE" -> {
                return DELETE;
            }
            default -> throw new IllegalArgumentException("Unknown HTTP method: " + method);
        }
    }
}
