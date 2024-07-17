package web;

public enum ViewPath {
    DEFAULT("/", "/index.html"),
    LOGIN("/login", "/login/index.html"),
    REGISTRATION("/registration", "/registration/index.html"),
    COMMENT("/comment", "/comment/index.html"),
    ARTICLE("/article", "/article/index.html"),

    BAD_REQUEST("/bad-request", "/error/400.html"),
    NOT_FOUND("/not-found", "/error/404.html"),
    INTERNAL_SERVER_ERROR("/internal-server-error", "/error/500.html");

    private final String requestUri;
    private final String filePath;

    ViewPath(String requestUri, String filePath) {
        this.requestUri = requestUri;
        this.filePath = filePath;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getFilePath() {
        return filePath;
    }

    public static ViewPath findByRequestUri(String uri) {
        for(ViewPath viewPath: ViewPath.values()) {
            if(viewPath.requestUri.equals(uri)) {
                return viewPath;
            }
        }
        return ViewPath.BAD_REQUEST;
    }
}
