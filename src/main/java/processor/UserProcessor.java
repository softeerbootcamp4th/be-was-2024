package processor;

import db.Database;
import handler.PostHandler;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestObject;

import java.util.HashMap;
import java.util.Map;

public class UserProcessor {

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
            String value = keyValue.length>1 ? keyValue[1] : "";
            map.put(key, value);
        }

        User user = new User(map.get("userId"),map.get("password"), map.get("name"),map.get("email"));
        try{
            Database.addUser(user);

        }catch(Exception e)
        {

        }

    }

    public User userFind(RequestObject requestObject) throws Exception {
        String paramLine = new String(requestObject.getBody());
        String[] pairs = paramLine.split("&");
        String[] idLine = pairs[0].split("=");
        String[] passwordLine = pairs[1].split("=");
        if(idLine.length==1||passwordLine.length==1)
        {
            throw new Exception("아이디와 비밀번호를 모두 입력해야 합니다");
        }
        User user = Database.findUserById(idLine[1]);

        if(user==null)
        {
            throw new Exception("해당하는 Id가 존재하지 않습니다");
        }
        if (!user.getPassword().equals(passwordLine[1])) {
            throw new Exception("비밀번호가 일치하지 않습니다");
        }

        return user;
    }
}
