package utils;

public class CookieUtil {
    public static boolean isExist(String sessionId) {
        if (sessionId == null) {
            return false;
        }
        if (sessionId.isEmpty()) {
            return false;
        } else{
            return true;
        }
    }
}
