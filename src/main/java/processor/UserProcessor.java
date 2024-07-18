package processor;

import db.UserDatabase;
import handler.SessionHandler;
import model.User;
import webserver.Request;

import java.util.HashMap;

public class UserProcessor {
    public void createUser(Request request) {
        HashMap<String, String> userData = request.parseBody();
        User user = User.from(userData);
        UserDatabase.addUser(user);
    }

    public boolean login(Request request) {
        HashMap<String, String> userData = request.parseBody();
        String userId = userData.get("userId");
        String password = userData.get("password");
        User user = UserDatabase.findUserById(userId);

        if (user != null) {
            if (password.equals(user.getPassword())) {
                // 로그인 성공
                SessionHandler.makeNewSessionId(user);
                return true;
            } else {
                // 로그인 실패. 패스워드 불일치
                return false;
            }
        } else {
            // 로그인 실패. 존재하지 않는 사용자
            return false;
        }
    }
}
