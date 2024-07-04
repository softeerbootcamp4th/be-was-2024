package webserver;

import db.Database;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class DynamicRequestProcess {
    public static String registration(String uri){
        String queryParameters = uri.split("\\?")[1];
        String[] queries = queryParameters.split("&");
        Map<String, String> map = new HashMap<>();
        for (String query : queries) {
            String[] entry = query.split("=");
            map.put(entry[0], entry[1]);
        }
        Database.addUser(new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email")));
        return "redirect:/index.html";
    }
}
