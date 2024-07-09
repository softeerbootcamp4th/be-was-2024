package webserver.mapper;

public class GetHandler {

    private static final String staticResourceDir = System.getProperty("staticResourceDir");

    public static String handle(String url) {
        switch (url) {
            case "/registration":
                return staticResourceDir + "/registration/index.html";
            case "/login":
                return staticResourceDir + "/login/index.html";
            default:
                return staticResourceDir + url;
        }
    }
}
