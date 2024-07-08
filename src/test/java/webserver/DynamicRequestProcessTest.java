package webserver;

import data.HttpRequestMessage;
import db.Database;
import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class DynamicRequestProcessTest {
    @Test
    @DisplayName("회원가입 테스트")
    public void testCreateUser(){
        //given
        HashMap<String, String> queryParam = new HashMap<>();
        queryParam.put("userId","abc");
        queryParam.put("password","123");
        queryParam.put("email","abc@gmail.com");
        queryParam.put("name","abc");

        //when
        DynamicRequestProcess.registration(queryParam);

        //then
        User abc = Database.findUserById("abc");
        Assertions.assertThat(Database.findUserById("abc")).isNotNull();
    }
}
