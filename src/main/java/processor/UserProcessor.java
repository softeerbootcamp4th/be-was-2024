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
            String value ="";
            try{
                 value = keyValue[1];
            }
            catch(IndexOutOfBoundsException e)
            {
                logger.debug(key +" 값이 비어 있습니다");
            }
            map.put(key, value);
        }

        User user = new User(map.get("userId"),map.get("password"), map.get("name"),map.get("email"));
        Database.addUser(user);
    }

    public boolean findUser(RequestObject requestObject)
    {
        String paramLine = new String(requestObject.getBody());
        String[] pairs = paramLine.split("&");
        String[] keyValue = pairs[0].split("=");
        User user = Database.findUserById(keyValue[1]);
        if(user==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
