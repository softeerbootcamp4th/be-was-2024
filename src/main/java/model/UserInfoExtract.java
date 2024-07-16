package model;

import java.util.HashMap;
import java.util.Map;

public class UserInfoExtract {

    public static User extractUserInfoFromBodyForCreate(String body) {
        String[] params = body.split("&");
        Map<String, String> userInfo = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            userInfo.put(keyValue[0], keyValue[1]);
        }
        String userId = userInfo.get("userId");
        String password = userInfo.get("password");
        String name = userInfo.get("name");
        String email = userInfo.get("email");

        User user = null;
        if (userId != null && password != null && name != null && email != null) {
            user = new User(userId, password, name, email);
        }
        return user;
    }

    public static User extractUserInfoFromBodyForLogin(String body) {
        String[] params = body.split("&");
        Map<String, String> userInfo = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            userInfo.put(keyValue[0], keyValue[1]);
        }
        String userId = userInfo.get("userId");
        String password = userInfo.get("password");

        User user = null;
        if (userId != null && password != null) {
            user = new User(userId, password);
        }
        return user;
    }

    public static String extractSessionIdFromHeader(String header){
        if(header.split(";").length == 1){
            return "";
        }
        String sessionId = header.split(";")[1].substring(5);
        return sessionId;
    }
}
