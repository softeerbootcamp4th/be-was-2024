package db;

import model.Post;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserH2Database {

    private final static String TABLE = "member";
    private final static String ID = "id";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "email";
    private final static String NAME = "name";

    public static void addUser(User user) {
        try {
            H2Database.insert("insert into "+TABLE+" ("+ID+", "+PASSWORD+", "+EMAIL+", "+NAME+") values (\'"+user.getUserId()+"\', \'" + user.getPassword() + "\', \'" + user.getEmail() + "\', \'" + user.getName() + "\')");
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Optional<User> findUserById(String userId) {
        try {
            ResultSet rs = H2Database.select("select * from "+TABLE+" where "+ID+" = " + userId);
            if (rs.next()) {
                String password = rs.getString(PASSWORD);
                String email = rs.getString(EMAIL);
                String name = rs.getString(NAME);
                return Optional.of(new User(userId, password, email, name));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
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
                users.add(new User(userId, password, email, name));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }

        return users;
    }

}
