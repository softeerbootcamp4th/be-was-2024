package common;

public class WebUtils {

    public static boolean isMethodHeader(String keyword) {
        return keyword.equals("GET") || keyword.equals("POST") || keyword.equals("PUT") || keyword.equals("PATCH") || keyword.equals("DELETE");
    }

    public static String getProperContentType(String extension) {
        switch(extension) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "application/javascript";
            case "ico":
                return "image/x-icon";
            case "png":
                return "image/png";
            case "jpg":
                return "image/jpeg";
            case "svg":
                return "image/svg+xml";
        }
        return null;
    }
}
