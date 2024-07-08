package util;

public enum UrlMapping {
    ROOT("/", "/index.html"),
    REGISTER("/register.html", "/registration/index.html"),
    LOGIN("/login.html", "/login/index.html"),
    ARTICLE("/article.html", "/article/index.html"),
    COMMENT("/comment.html", "/comment/index.html"),
    MAIN("/main.html", "/main/index.html");

    private final String originalUrl;
    private final String mappedUrl;

    UrlMapping(String originalUrl, String mappedUrl) {
        this.originalUrl = originalUrl;
        this.mappedUrl = mappedUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getMappedUrl() {
        return mappedUrl;
    }

    public static String map(String url) {
        for (UrlMapping mapping : values()) {
            if (mapping.getOriginalUrl().equals(url)) {
                return mapping.getMappedUrl();
            }
        }
        return url;
    }
}
