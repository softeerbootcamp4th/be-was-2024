package user;

import db.UserDatabase;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTest {

    @BeforeEach
    void setUp() {
        UserDatabase.initialize();
    }

    @Test
    @DisplayName("유저 저장 테스트 (성공 case)")
    public void userRegistrationTestSuccess() throws Exception {
        // when
        UserDatabase.addUser(new User("testId1", "testPwd1", "testName1", "testEmail1"));
        UserDatabase.addUser(new User("testId2", "testPwd2", "testName2", "testEmail2"));
        UserDatabase.addUser(new User("testId3", "testPwd3", "testName3", "testEmail3"));

        // then
        Assertions.assertEquals(UserDatabase.findAll().size(), 3);
        Assertions.assertEquals(UserDatabase.findUserById("testId1").getPassword(), "testPwd1");
        Assertions.assertEquals(UserDatabase.findUserById("testId1").getName(), "testName1");
        Assertions.assertEquals(UserDatabase.findUserById("testId1").getEmail(), "testEmail1");
    }

    @Test
    @DisplayName("유저 저장 테스트 (실패 - 중복 회원 Id)")
    public void userRegistrationTestFailOnDuplicate() throws Exception {
        // when
        UserDatabase.addUser(new User("testId1", "testPwd1", "testName1", "testEmail1"));
        UserDatabase.addUser(new User("testId1", "testPwd2", "testName2", "testEmail2"));

        // then
        Assertions.assertEquals(UserDatabase.findAll().size(), 1);
        Assertions.assertNotEquals(UserDatabase.findAll().size(), 2);
        // 현재 Database 저장 로직상 나중에 들어간 회원이 저장된다.
        Assertions.assertEquals(UserDatabase.findUserById("testId1").getPassword(), "testPwd2");
        Assertions.assertEquals(UserDatabase.findUserById("testId1").getName(), "testName2");
        Assertions.assertEquals(UserDatabase.findUserById("testId1").getEmail(), "testEmail2");
    }
}
