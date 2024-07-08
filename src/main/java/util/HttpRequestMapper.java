package util;

/**
 * 요청에 대한 매핑을 담당하는 enum 클래스
 */
public enum HttpRequestMapper {

    // 200
    STATIC("/index.html", "GET"),

    // 302
    USER_SIGNUP("/user/create", "POST"),
    REGISTER("/register.html", "GET"),

    // 400
    MESSAGE_NOT_ALLOWED("/notallow", "GET");

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
        return STATIC;
    }
}
