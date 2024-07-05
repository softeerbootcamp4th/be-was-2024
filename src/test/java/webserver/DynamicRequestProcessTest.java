package webserver;

import db.Database;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DynamicRequestProcessTest {
    @Test
    @DisplayName("회원가입 테스트")
    public void testCreateUser(){
        //given

        //when
        DynamicRequestProcess.registration("/create?userId=abc&email=abc%40naver.com&name=aa&password=1234");

        //then
        User abc = Database.findUserById("abc");
        System.out.println(abc);
        Assertions.assertThat(Database.findUserById("abc")).isNotNull();
    }
}
