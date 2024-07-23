package utils;

public class CookieUtil {
    public static boolean isExist(String sessionId) {
        if (sessionId == null) {
            return false;
        }
        return !sessionId.isEmpty();
    }

    public static String getCookie(String cookie) {
        if (cookie == null) {
            return null;
        }
        String[] cookies = cookie.split(";");
        for (String c : cookies) {
            if (c.contains("SID")) {
                c = c.trim();
                return c.split("=")[1];
            }
        }
        return null;
    }
}
