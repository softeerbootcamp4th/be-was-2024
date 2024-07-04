package webserver;

import db.Database;
import model.User;

import java.util.HashMap;

public class LogicProcessor {
    public void createUser(Request request) {
        HashMap<String, String> userData = request.parseQueryString();
        User user = User.from(userData);
        System.out.println(user);
        Database.addUser(user);
    }
}
