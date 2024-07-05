package util;

public class UrlMapper {

    /**
     * Maps the given URL to a new URL.
     *
     * @param url the URL to map
     * @return the new URL
     */
    public String mapUrl(String url) {
        return switch (url) {
            case "/" -> "/index.html";
            case "/register.html" -> "/registration/index.html";
            case "/login.html" -> "/login/index.html";
            case "/article.html" -> "/article/index.html";
            case "/comment.html" -> "/comment/index.html";
            case "/main.html" -> "/main/index.html";
            default -> url;
        };
    }
}
