package webserver.mapper;

public class GetHandler {

    public static String handle(String url) {
        switch (url) {
            case "/registration":
                return "/registration/index.html";
            default:
                return url;
        }
    }
}
