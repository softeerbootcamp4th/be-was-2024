package webserver;

import db.Database;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class DynamicRequestProcess {
    public static String registration(Map<String ,String> queryParam){
        Database.addUser(new User(queryParam.get("userId"), queryParam.get("password"), queryParam.get("name"), queryParam.get("email")));
        return "redirect:/index.html";
    }
}
