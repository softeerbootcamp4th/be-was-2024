package test;

import db.Database;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    @DisplayName("유저 회원가입 요청 시 데이터베이스에 올바르게 저장되는지 확인할 수 있는 테스트입니다.")
    void test() {
//        URL requestUrl = new URL("http://localhost:8080/user/create?userId=testId&name=myName&password=myPassword&email=myEmail");
//        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
//        connection.setRequestMethod("GET");
//        connection.getInputStream();


        Database.addUser(new User("testId", "testPassword", "testName", "testEmail"));

        User user = Database.findUserById("testId");


        Assertions.assertEquals("testId", user.getUserId());
        Assertions.assertEquals("testPassword", user.getPassword());
        Assertions.assertEquals("testName", user.getName());
        Assertions.assertEquals("testEmail", user.getEmail());

    }
}
