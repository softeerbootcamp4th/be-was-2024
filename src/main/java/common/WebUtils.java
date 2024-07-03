package common;

public class WebUtils {

    public static boolean isWebRequest(String keyword) {
        return keyword.equals("GET") || keyword.equals("POST") || keyword.equals("PUT") || keyword.equals("PATCH") || keyword.equals("DELETE");
    }
}
