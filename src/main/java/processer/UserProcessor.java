package processer;

import db.Database;
import http.HttpStatus;
import model.User;
import util.exception.CustomException;


public class UserProcessor {
    public static void createUser(String userId, String name, String password, String email)  {

        User user = Database.findUserById(userId);
        if (user != null) {
            throw new CustomException(HttpStatus.CONFLICT, "사용할 수 없는 아이디입니다.");
        }

        User newUser = new User(userId, name, password, email);
        Database.addUser(newUser);
    }

    public static void loginUser(String userId, String password){
        User user = Database.findUserById(userId);
        if (user == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다.");
        }
        if (!user.getPassword().equals(password)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "비밀번호를 확인해주세요.");
        }
    }

}
