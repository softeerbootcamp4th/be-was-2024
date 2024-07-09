package util;

/**
 * 요청에 대한 매핑을 담당하는 enum 클래스
 */
public enum HttpRequestMapper {

    // 200
    ROOT("/", "GET"),
    INDEX_HTML("/index.html", "GET"),
    USER_LOGIN_FAIL("/user/login_failed.html", "GET"),
    USER_LOGIN("/login", "GET"),

    // 302
    SIGNUP_REQUEST("/user/create", "POST"),
    LOGIN_REQUEST("/user/login", "POST"),
    LOGOUT_REQUEST("/user/logout", "POST"),
    REGISTER("/registration", "GET"),

    // 400
    MESSAGE_NOT_ALLOWED("/notallow", "GET"),

    // 500
    ERROR("/error", "GET");

    private final String path;
    private final String method;

    HttpRequestMapper(String path, String method) {
        this.path = path;
        this.method = method;
    }

    public static HttpRequestMapper of(String path, String method) {
        for (HttpRequestMapper mapper : values()) {
            if(mapper.path.equals(path)){
                if(mapper.method.equals(method)){
                    return mapper;
                }
                return MESSAGE_NOT_ALLOWED; // 요청 경로는 찾았으나 메서드가 없는 경우
            }
        }
        return ERROR;
    }
}
