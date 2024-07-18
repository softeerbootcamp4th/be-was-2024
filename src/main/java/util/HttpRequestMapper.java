package util;

import controller.*;

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
    public static Controller getController(String path, String method) {
        for (HttpRequestMapper mapper : values()) {
            if(mapper.path.equals(path)){
                if(mapper.method.equals(method)){
                    return controllerMapper(mapper);
                }
                return controllerMapper(METHOD_NOT_ALLOWED); // 요청 경로는 찾았으나 메서드가 없는 경우
            }
        }
        return controllerMapper(SERVER_ERROR); // 요청 경로가 없는 경우
    }

    /**
     * HttpRequestMapper에 따라 Controller를 반환하는 메서드
     * @param mapper
     * @return Controller
     */
    private static Controller controllerMapper(HttpRequestMapper mapper) {
        return switch (mapper) {
            case ROOT, DEFAULT_PAGE -> new DefaultController();
            case ARTICLE, ARTICLE_CREATE -> new ArticleController();
            case USER_LIST -> new UserListController();
            case SIGNUP_REQUEST, REGISTER -> new SignUpController();
            case LOGIN, LOGIN_REQUEST -> new LoginController();
            case LOGIN_FAIL -> new LoginFailController();
            case LOGOUT_REQUEST -> new LogoutController();
            default -> new ErrorController(mapper);
        };
    }

    public String getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }
}