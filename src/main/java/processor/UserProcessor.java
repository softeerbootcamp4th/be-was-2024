package processor;

import db.Database;
import handler.GetHandler;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestObject;

import java.util.Map;

public class UserProcessor {

    private static final Logger logger = LoggerFactory.getLogger(UserProcessor.class);

    private UserProcessor() {}

    private static class LazyHolder{
        private static final UserProcessor INSTANCE = new UserProcessor();
    }


    public static UserProcessor getInstance()
    {
        return LazyHolder.INSTANCE;
    }

    public void userCreate(RequestObject requestObject)
    {
        Map<String,String> map = requestObject.getParams();
        User user = new User(map.getOrDefault("userId",""),map.getOrDefault("password"," "),
                             map.getOrDefault("name"," "),map.getOrDefault("email"," "));
        Database.addUser(user);

    }
}
