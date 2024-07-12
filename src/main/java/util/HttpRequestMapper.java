package util;

/**
 * 요청에 대한 매핑을 담당하는 enum 클래스
 */
public enum HttpRequestMapper {

    // OK
    DEFAULT_PAGE("/index.html", "GET"),
    LOGIN_FAIL("/user/login_failed.html", "GET"),
    LOGIN("/user/login.html" , "GET"),
    SIGNUP("/user/form.html", "GET"),
    USER_LIST("/user/list", "GET"),
    ARTICLE("/article/index.html", "GET"),

    // Redirect
    ROOT("/", "GET"),
    REGISTER("/registration", "GET"),
    SIGNUP_REQUEST("/user/create", "POST"),
    LOGIN_REQUEST("/user/login", "POST"),
    LOGOUT_REQUEST("/user/logout", "POST"),

    // Error
    MESSAGE_NOT_ALLOWED("/notallow", "GET"),
    NOT_FOUND("/error", "GET");

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
        return NOT_FOUND;
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
}
