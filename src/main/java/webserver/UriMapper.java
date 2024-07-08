package webserver;

import data.HttpRequestMessage;

public class UriMapper {
    public static String mapUri(HttpRequestMessage httpRequestMessage){
        //Exact Matching
        return switch (httpRequestMessage.getUri()){
            case "/create" -> DynamicRequestProcess.registration(httpRequestMessage.getQueryParam());
            case "/registration.html" -> "src/main/resources/static/registration/index.html";
            default -> "src/main/resources/static" + httpRequestMessage.getUri();
        };
    }
}
