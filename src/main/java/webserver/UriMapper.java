package webserver;

import data.HttpRequestMessage;

public class UriMapper {
    public static String mapUri(HttpRequestMessage httpRequestMessage) throws RuntimeException {
        //Exact Matching
        if (httpRequestMessage.getMethod().equals("GET")){
            return switch (httpRequestMessage.getUri()){
                case "/registration.html" -> "src/main/resources/static/registration/index.html";
                case "/index.html" -> "src/main/resources/static/index.html";
                default -> "src/main/resources/static" + httpRequestMessage.getUri();
            };
        }
        else if (httpRequestMessage.getMethod().equals("POST")){
            return switch (httpRequestMessage.getUri()){
                case "/create" -> DynamicRequestProcess.registration(httpRequestMessage);
                default -> throw new RuntimeException();
            };
        }
        throw new RuntimeException();
    }
}
