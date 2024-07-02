package webserver;

import db.Database;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class DynamicRequestProcess {
    public static String registration(String uri){
        String queryParameters = uri.split("\\?")[1];
        String[] queries = queryParameters.split("&");
        Map<String, String> mp= new HashMap<>();
        for (String query : queries) {
            String[] entry = query.split("=");
            mp.put(entry[0], entry[1]);
        }
        Database.addUser(new User(mp.get("userId"), mp.get("password"), mp.get("name"), mp.get("email")));
        return "/index.html";
    }
}
