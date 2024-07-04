package webserver;

public class UriMapper {
    public static String mapUri(String uri){
        //동적 기능
        if (uri.startsWith("/create")){
            return DynamicRequestProcess.registration(uri);
        }

        //정적 페이지 리턴
        return switch (uri){
            case "/registration.html" -> "src/main/resources/static/registration/index.html";
            default -> "src/main/resources/static" + uri;
        };
    }
}
