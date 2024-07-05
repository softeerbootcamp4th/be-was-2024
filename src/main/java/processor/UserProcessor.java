package processor;

import db.Database;
import model.User;
import util.RequestObject;

import java.util.Map;

public class UserProcessor {


    public static void userCreate(RequestObject requestObject)
    {
        Map<String,String> map = requestObject.getParams();
        User user = new User(map.getOrDefault("userId",""),map.getOrDefault("password"," "),
                             map.getOrDefault("name"," "),map.getOrDefault("email"," "));
        Database.addUser(user);

    }
}
