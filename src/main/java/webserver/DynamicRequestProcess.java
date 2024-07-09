package webserver;

import data.HttpRequestMessage;
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
        System.out.println("findUser : " + Database.findUserById("abc"));
        return "redirect:/index.html";
    }
}
