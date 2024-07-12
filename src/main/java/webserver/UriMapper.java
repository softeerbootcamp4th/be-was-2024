package webserver;

import data.HttpRequestMessage;
import data.HttpResponseMessage;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UriMapper {
    public static HttpResponseMessage mapUri(HttpRequestMessage httpRequestMessage) throws RuntimeException, IOException {
        //Exact Matching
        if (httpRequestMessage.getMethod().equals("GET")){
            return switch (httpRequestMessage.getUri()){
                case "/", "/index.html" -> DynamicRequestProcess.home(httpRequestMessage);
                case "/user/list" -> DynamicRequestProcess.userList(httpRequestMessage);
                case "/registration.html" -> staticRequestProcess("src/main/resources/static/registration/index.html");
                case "/login" -> staticRequestProcess("src/main/resources/static/login/index.html");
                default -> staticRequestProcess("src/main/resources/static" + httpRequestMessage.getUri());
            };
        }
        else if (httpRequestMessage.getMethod().equals("POST")){
            return switch (httpRequestMessage.getUri()){
                case "/logout" -> DynamicRequestProcess.logout(httpRequestMessage);
                case "/create" -> DynamicRequestProcess.registration(httpRequestMessage);
                case "/user/login" -> DynamicRequestProcess.login(httpRequestMessage);
                default -> throw new RuntimeException();
            };
        }
        throw new RuntimeException();
    }

    public static HttpResponseMessage staticRequestProcess(String path) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", findContentType(path.substring(path.lastIndexOf('.') + 1)));
        byte[] bytes = FileUtil.readAllBytesFromFile(new File(path));
        return new HttpResponseMessage("200",headers,bytes);
    }

    private static String findContentType(String ext){
        return switch (ext) {
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "html" -> "text/html";
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "ico" -> "image/x-icon";
            case "svg" -> "image/svg+xml";
            default -> "text/plain";
        };
    }
}
