package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.UserPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * H2 를 활용하여 User 데이터를 저장하는 클래스
 */
public class UserH2Database {

    private final static String TABLE = "member";
    private final static String ID = "id";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "email";
    private final static String NAME = "name";

    public static final Logger logger = LoggerFactory.getLogger(UserPlugin.class);

    /**
     * user를 추가하는 메소드
     * @param user
     */
    public static void addUser(User user) {
        try {
            String sql = "insert into "+TABLE+" ("+ID+", "+PASSWORD+", "+EMAIL+", "+NAME+") values (\'"+user.getUserId()+"\', \'" + user.getPassword() + "\', \'" + user.getEmail() + "\', \'" + user.getName() + "\')";
            logger.debug("sql:{}", sql);
            H2Database.insert(sql);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * id 를 기반으로 유저를 반환하는 메소드
     * @param userId
     * @return
     */
    public static Optional<User> findUserById(String userId) {
        try {
            ResultSet rs = H2Database.select("select * from "+TABLE+" where "+ID+" = \'" + userId+"\'");
            if (rs.next()) {
                String password = rs.getString(PASSWORD);
                String email = rs.getString(EMAIL);
                String name = rs.getString(NAME);
                return Optional.of(new User(userId, password, name, email));
            }
        }catch (SQLException e){
            logger.debug(e.getMessage());
            return Optional.empty();
        }
        return Optional.empty();
    }

    /**
     * 저장되어 있는 모든 유저를 반환하는 메소드
     * @return
     */
    public static Collection<User> findAll() {

        Collection<User> users = new ArrayList<>();
        try {
            ResultSet rs = H2Database.select("select * from "+TABLE);
            if (rs.next()) {
                String userId = rs.getString(ID);
                String password = rs.getString(PASSWORD);
                String email = rs.getString(EMAIL);
                String name = rs.getString(NAME);
                users.add(new User(userId, password, name, email));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return users;
    }

}
