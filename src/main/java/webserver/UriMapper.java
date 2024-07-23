package webserver;

import data.HttpRequestMessage;
import data.HttpResponseMessage;
import exception.BadUrlException;
import util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class UriMapper {
    public static HttpResponseMessage mapUri(HttpRequestMessage httpRequestMessage) throws RuntimeException, IOException {
        //Exact Matching
        String method = httpRequestMessage.getMethod();
        String uri = httpRequestMessage.getUri();
        return switch (uri) {
                case "/", "/index.html" -> DynamicRequestProcess.home(httpRequestMessage);
                case "/article" -> DynamicRequestProcess.article(httpRequestMessage);
                case "/user/list" -> DynamicRequestProcess.userList(httpRequestMessage);
                case "/registration" -> staticRequestProcess("src/main/resources/static/registration/index.html");
                case "/login" -> staticRequestProcess("src/main/resources/static/login/index.html");
                case "/logout" -> DynamicRequestProcess.logout(httpRequestMessage);
                case "/post" -> DynamicRequestProcess.postArticle(httpRequestMessage);
                case "/create" -> DynamicRequestProcess.registration(httpRequestMessage);
                case "/user/login" -> DynamicRequestProcess.login(httpRequestMessage);
                default -> staticRequestProcess("src/main/resources/static" + uri);
        };
    }

    public static HttpResponseMessage errorResponseProcess(String code) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        String path = "src/main/resources/static/error/" + code + ".html";
        headers.put("Content-Type", findContentType("html") + ";charset=utf-8");
        byte[] bytes = FileUtil.readAllBytesFromFile(new File(path));
        headers.put("Content-Length", String.valueOf(bytes.length));
        return new HttpResponseMessage(code,headers,bytes);
    }

    public static HttpResponseMessage staticRequestProcess(String path) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", findContentType(path.substring(path.lastIndexOf('.') + 1)));
        byte[] bytes;
        try {
             bytes = FileUtil.readAllBytesFromFile(new File(path));
        }
        catch (Exception e) {
            throw new BadUrlException("Not Found");
        }
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
