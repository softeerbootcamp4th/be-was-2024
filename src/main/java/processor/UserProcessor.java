package processor;

import db.Database;
import model.User;
import webserver.Request;

import java.util.HashMap;

public class UserProcessor {
    public void createUser(Request request) {
        HashMap<String, String> userData = request.parseBody();
        User user = User.from(userData);
        System.out.println(user);
        Database.addUser(user);
    }
}
