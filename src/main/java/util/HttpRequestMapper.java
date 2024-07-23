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
    ARTICLE_CREATE("/article/create", "POST"),

    // Error
    METHOD_NOT_ALLOWED("/notAllow", "GET"),
    NOT_FOUND("/notFound", "GET"),
    SERVER_ERROR("/error", "GET");

    private final String path;
    private final String method;

    HttpRequestMapper(String path, String method) {
        this.path = path;
        this.method = method;
    }

    /**
     * 요청 경로와 메서드에 대한 HttpRequestMapper를 반환하는 메서드
     * @param path
     * @param method
     * @return HttpRequestMapper
     */
    public static HttpRequestMapper of(String path, String method) {
        for (HttpRequestMapper mapper : values()) {
            if(mapper.path.equals(path)){
                if(mapper.method.equals(method)){
                    return mapper;
                }
                return METHOD_NOT_ALLOWED; // 요청 경로는 찾았으나 메서드가 없는 경우
            }
        }
        return SERVER_ERROR; // 요청 경로가 없는 경우
    }

    /**
     * 로그인/로그아웃 요청인지 확인하는 메서드
     * @param path
     * @param method
     * @return boolean
     */
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
