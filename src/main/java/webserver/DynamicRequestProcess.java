package webserver;

import data.HttpRequestMessage;
import data.HttpResponseMessage;
import db.Database;
import db.Session;
import model.User;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DynamicRequestProcess {
    public static HttpResponseMessage registration(HttpRequestMessage httpRequestMessage){
        Map<String,String> map = new HashMap<>();
        String body = new String(httpRequestMessage.getBody());
        String[] bodySplit = body.split("&");
        for (String entry : bodySplit) {
            String[] keyValue = entry.split("=");
            map.put(keyValue[0],keyValue[1]);
        }
        Database.addUser(new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email")));
        map.clear();
        map.put("Location","/index.html");
        return new HttpResponseMessage("303",map,null);
    }
    public static HttpResponseMessage login(HttpRequestMessage httpRequestMessage){
        Map<String,String> map = new HashMap<>();
        String requestBody = new String(httpRequestMessage.getBody());
        String[] bodySplit = requestBody.split("&");
        for (String entry : bodySplit) {
            String[] keyValue = entry.split("=");
            map.put(keyValue[0],keyValue[1]);
        }
        User user = Database.findUserById(map.get("userId"));
        if (user == null || !user.getPassword().equals(map.get("password"))){
            map.clear();
            map.put("Location","/login");
            return new HttpResponseMessage("303", map,null);
        }
        map.clear();
        String sessionId = UUID.randomUUID().toString();
        map.put("Set-Cookie","SID="+sessionId + "; Path=/");
        map.put("Location","/index.html");
        Session.addNewSession(sessionId,user);
        return new HttpResponseMessage("303",map,null);
    }
}
