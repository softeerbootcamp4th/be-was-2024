package webserver;

import data.HttpRequestMessage;
import data.HttpResponseMessage;
import db.Database;
import model.User;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DynamicRequestProcess {
    public static String registration(HttpRequestMessage httpRequestMessage){
        Map<String,String> map = new HashMap<>();
        String body = new String(httpRequestMessage.getBody());
        String[] bodySplit = body.split("&");
        for (String entry : bodySplit) {
            String[] keyValue = entry.split("=");
            map.put(keyValue[0],keyValue[1]);
        }
        Database.addUser(new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email")));
        return "redirect:/index.html";
    }
    public static HttpResponseMessage login(HttpRequestMessage httpRequestMessage){
        Map<String,String> headers = new HashMap<>();
        String requestBody = new String(httpRequestMessage.getBody());
        String[] bodySplit = requestBody.split("&");
        for (String entry : bodySplit) {
            String[] keyValue = entry.split("=");
            headers.put(keyValue[0],keyValue[1]);
        }
        return new HttpResponseMessage("200",headers,null);
    }
}
