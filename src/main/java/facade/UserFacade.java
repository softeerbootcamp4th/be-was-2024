package facade;

import db.UserH2Database;
import model.User;

import java.util.Map;

/**
 * 유저 관련 퍼사드
 */
public class UserFacade {

    /**
     * 새로운 유저를 생성하고, DB에 저장한다.
     * @param user 생성할 유저 정보
     * @return 생성된 유자
     */
    public static User createUser(User user) {
        return UserH2Database.addUser(new User(user.getUserId(), user.getPassword(), user.getName(), user.getEmail()));
    }

    /**
     * DB에서 user ID로 유저를 찾아서 반환
     * @param userId 찾을 유저의 id
     * @return 찾은 유저 정보
     */
    public static User findUserByUserId(String userId) {
        return UserH2Database.findUserById(userId);
    }

    /**
     * 데이터베이스에 유저가 존재하는지 검증
     * @param map HTTP body 정보
     * @return 유저가 존재하는지 여부 반환
     */
    public static boolean isUserExist(Map<String, String> map) {
        User findUser = UserH2Database.findUserById(map.get("userId"));
        return findUser!=null && findUser.getPassword().equals(map.get("password"));
    }
}
