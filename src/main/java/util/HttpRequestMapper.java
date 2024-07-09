package util;

/**
 * 요청에 대한 매핑을 담당하는 enum 클래스
 */
public enum HttpRequestMapper {
    USER_SIGNUP("/user/create", "GET"),
    REGISTER("/register.html", "GET"),
    STATIC("/index.html", "GET");

    private final String path;
    private final String method;

    HttpRequestMapper(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public static HttpRequestMapper of(String path, String method) {
        for (HttpRequestMapper mapper : values()) {
            if (mapper.path.equals(path) && mapper.method.equals(method)) {
                return mapper;
            }
        }
        return STATIC;
    }
}
