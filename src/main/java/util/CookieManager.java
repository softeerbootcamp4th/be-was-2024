package util;

import model.User;

import java.util.UUID;

public class CookieManager {

    private CookieManager() {
    }

    public static void setUserCookie(HttpResponseObject response, User user) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6);
        String cookie = "sid=" + uuid + "; Path=/";
        user.setSessionId(uuid);
        response.putHeader("Set-Cookie", cookie);
    }

    public static String parseUserCookie(String cookie) {
        String[] cookies = cookie.split(";\\s+"); // ;로 시작되는 1개 이상의 공백문자를 찾아서 split
        for (String c : cookies) {
            if (c.contains("sid")) {
                int idx = c.indexOf("=");
                if(idx != -1){
                    return c.substring(idx + 1);
                }
            }
        }
        return null;
    }

    public static void deleteUserCookie(HttpResponseObject response, String sessionId){
        response.putHeader("Set-Cookie", "sid=" + sessionId + "; Path=/; Max-Age=0");
    }
}
