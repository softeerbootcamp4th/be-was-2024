package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.UserPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserH2Database {

    private final static String TABLE = "member";
    private final static String ID = "id";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "email";
    private final static String NAME = "name";

    public static final Logger logger = LoggerFactory.getLogger(UserPlugin.class);

    public static void addUser(User user) {
        try {
            String sql = "insert into "+TABLE+" ("+ID+", "+PASSWORD+", "+EMAIL+", "+NAME+") values (\'"+user.getUserId()+"\', \'" + user.getPassword() + "\', \'" + user.getEmail() + "\', \'" + user.getName() + "\')";
            logger.debug("sql:{}", sql);
            H2Database.insert(sql);
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
