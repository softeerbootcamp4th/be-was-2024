package common;

public class WebUtils {

    public static boolean isMethodHeader(String method) {
        return method.equals("GET") || method.equals("POST") || method.equals("PUT") || method.equals("PATCH") || method.equals("DELETE");
    }

    public static boolean isGetRequest(String method) {
        return method.equals("GET");
    }

    public static boolean isPostRequest(String method) {
        return method.equals("POST");
    }

    public static String getProperContentType(String extension) {
        return switch (extension) {
            case "html" -> "text/html";
            case "css" -> "text/css";
            case "js" -> "application/javascript";
            case "ico" -> "image/x-icon";
            case "png" -> "image/png";
            case "jpg" -> "image/jpeg";
            case "svg" -> "image/svg+xml";
            default -> null;
        };
    }

    public static boolean isRESTRequest(String path) {
        return path.split("\\.").length==1;
    }
}
