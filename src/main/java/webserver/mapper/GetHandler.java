package webserver.mapper;

public class GetHandler {

    public static String handle(String url) {
        switch (url) {
            case "/registration":
                return "/registration/index.html";
            case "/login":
                return "/login/index.html";
            default:
                return url;
        }
    }
}
