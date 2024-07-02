package common;

public class WebUtils {

    public static boolean isMethodHeader(String keyword) {
        return keyword.equals("GET") || keyword.equals("POST") || keyword.equals("PUT") || keyword.equals("PATCH") || keyword.equals("DELETE");
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
}
