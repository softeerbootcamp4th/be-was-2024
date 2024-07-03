package test;

import db.Database;
import model.User;
import org.junit.jupiter.api.Assertions;

import java.net.HttpURLConnection;
import java.net.URL;

public class UserRegistrationTest {
    private static Database database = Database.getInstance();
    static void test() {
        try {
            URL requestUrl = new URL("http://localhost:8080/user/create?userId=myId&password=myPassword&name=myName&email=myEmail");
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            User user = Database.findUserById("myId");

            Assertions.assertEquals("myId", user.getUserId());
            Assertions.assertEquals("myName", user.getName());
            Assertions.assertEquals("myPassword", user.getPassword());
            Assertions.assertEquals("myEmail", user.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        test();
    }

}
