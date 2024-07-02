package webserver;

public class WebAdapter {

    public String resolveRequestUri(String restUri) {
        return switch(restUri) {
            case "/login" -> "/login/index.html";
            case "/registration" -> "/registration/index.html";
            case "/comment" -> "/comment/index.html";
            case "/article" -> "/article/index.html";
            default -> "/index.html";
        };
    }
}
