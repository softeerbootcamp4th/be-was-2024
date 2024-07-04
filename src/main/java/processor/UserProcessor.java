package processor;

import db.Database;
import model.User;
import util.RequestObject;

import java.util.Map;

public class UserProcessor {


    public static void userCreate(RequestObject requestObject)
    {
        Map<String,String> map = requestObject.getParams();
        User user = new User(map.get("userId"),map.get("password"),map.get("name"),map.get("email"));
        Database.addUser(user);
    }
}
