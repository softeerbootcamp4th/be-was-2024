package model;

import java.util.HashMap;
import java.util.Map;

public class UserInfoExtract {

    public static Map<String, String> extractUserInfoFromBody(String body) {
        String[] params = body.split("&");
        Map<String, String> userInfo = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            userInfo.put(keyValue[0], keyValue[1]);
        }
        return userInfo;
    }
}
