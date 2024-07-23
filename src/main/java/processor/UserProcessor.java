package processor;

import db.Database;
import model.User;
import util.RequestObject;

import java.util.HashMap;
import java.util.Map;



/**
 * User객체를 처리하기 위한 class
 */
public class UserProcessor {

    private UserProcessor() {}

    private static class LazyHolder{
        private static final UserProcessor INSTANCE = new UserProcessor();
    }



    /**
     * LazyHolder 방식으로 싱글톤 구현
     */
    public static UserProcessor getInstance()
    {
        return LazyHolder.INSTANCE;
    }



    /**
     * requestObject에 담겨있는 user생성을 위한 값들을 파싱 후 User를 생성
     * @param requestObject 넘어온 requestObject
     */
    public void userCreate(RequestObject requestObject) {
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



    /**
     * 로그인 시 넘어온 Id값 Password로 해당하는 User가 있는지 찾고 User를 반환
     */
    public User userFind(RequestObject requestObject) throws Exception {
        String paramLine = new String(requestObject.getBody());
        String[] pairs = paramLine.split("&");
        String[] idLine = pairs[0].split("=");
        String[] passwordLine = pairs[1].split("=");
        if(idLine.length==1||passwordLine.length==1) {
            throw new Exception("아이디와 비밀번호를 모두 입력해야 합니다");
        }
        User user = Database.findUserById(idLine[1]);

        if(user==null) {
            throw new Exception("해당하는 Id가 존재하지 않습니다");
        }
        if (!user.getPassword().equals(passwordLine[1])) {
            throw new Exception("비밀번호가 일치하지 않습니다");
        }

        return user;
    }
}
