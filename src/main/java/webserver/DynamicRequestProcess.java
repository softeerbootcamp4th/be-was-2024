package webserver;

import data.HttpRequestMessage;
import data.HttpResponseMessage;
import db.Database;
import db.Session;
import handler.ViewHandler;
import model.User;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
        Database.addUser(new User(map.get("userId"), map.get("password"), URLDecoder.decode(map.get("name"),StandardCharsets.UTF_8), map.get("email")));
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

    public static HttpResponseMessage home(HttpRequestMessage httpRequestMessage) throws IOException {
        Map<String, String> cookies = httpRequestMessage.getCookies();
        HashMap<String, String> headers = new HashMap<>();
        String sid = cookies.get("SID");
        if (sid == null){
            return UriMapper.staticRequestProcess("src/main/resources/static/index.html");
        }
        User user = Session.getUser(sid);
        if (user == null) return UriMapper.staticRequestProcess("src/main/resources/static/index.html");
        Map<String,String> param = new HashMap<>();
        param.put("userName", user.getName());
        String loginedHomeView = ViewHandler.viewProcess("src/main/resources/static/main/index.html",param);
        System.out.println(loginedHomeView);
        return new HttpResponseMessage("200",headers,loginedHomeView.getBytes());
    }
}
