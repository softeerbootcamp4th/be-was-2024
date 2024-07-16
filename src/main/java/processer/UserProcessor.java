package processer;

import db.UserDatabase;
import http.HttpStatus;
import model.User;
import util.exception.CustomException;

import java.util.Optional;


public class UserProcessor {
    private static final UserDatabase userDatabase = new UserDatabase();


    public static void createUser(String userId, String name, String password, String email)  {

        Optional<User> user = userDatabase.findUserById(userId);
        if (user.isPresent()) {
            throw new CustomException(HttpStatus.CONFLICT, "사용할 수 없는 아이디입니다.");
        }

        User newUser = new User(userId, name, password, email);
        userDatabase.addUser(newUser);
    }

    public static void loginUser(String userId, String password){
        Optional<User> user = userDatabase.findUserById(userId);
        if (user.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "존재하지 않는 아이디입니다.");
        }
        if (!user.get().getPassword().equals(password)) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "비밀번호를 확인해주세요.");
        }
    }

}
