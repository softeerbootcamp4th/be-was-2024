package util;

/**
 * 요청에 대한 매핑을 담당하는 enum 클래스
 */
public enum HttpRequestMapper {

    // 200
    INDEX_HTML("/index.html", "GET", "200"),
    USER_LOGIN_FAIL("/user/login_failed.html", "GET", "200"),

    // 302
    ROOT("/", "GET", "302"),
    REGISTER("/registration", "GET", "302"),
    LOGIN("/login", "GET", "302"),
    SIGNUP_REQUEST("/user/create", "POST", "302"),
    LOGIN_REQUEST("/user/login", "POST", "302"),
    LOGOUT_REQUEST("/user/logout", "POST", "302"),

    // 400
    MESSAGE_NOT_ALLOWED("/notallow", "GET", "405"),

    // 500
    ERROR("/error", "GET", "500");

    private final String path;
    private final String method;
    private final String code;


    HttpRequestMapper(String path, String method, String code) {
        this.path = path;
        this.method = method;
        this.code = code;
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

    public static boolean isAuthRequest(String path, String method) {
        return LOGIN_REQUEST.equals(of(path, method)) || LOGOUT_REQUEST.equals(of(path, method));
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public String getCode() {
        return code;
    }
}
