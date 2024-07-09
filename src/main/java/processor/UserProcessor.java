package processor;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestObject;

import java.util.HashMap;
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
        Map<String,String> map =  new HashMap<>();
        String paramLine = new String(requestObject.getBody());
        String[] pairs = paramLine.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            String key = keyValue[0];
            String value = keyValue[1];
            map.put(key, value);
        }

        User user = new User(map.getOrDefault("userId",""),map.getOrDefault("password"," "),
                map.getOrDefault("name"," "),map.getOrDefault("email"," "));
        logger.debug(user.toString());
        Database.addUser(user);
    }
}
