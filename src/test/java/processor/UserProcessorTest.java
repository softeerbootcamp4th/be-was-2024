package processor;

import db.Database;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.RequestObject;

import java.util.Collection;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;


class UserProcessorTest {


    @DisplayName("User가 정상적으로 생성되고 저장되는지 확인하는 테스트")
    @Test
    void 저장Test()
    {
        //given
        RequestObject requestObject = new RequestObject("GET /user/create?userId=1234&name=bjh3311&password=1q2w&email=abc@naver.com HTTP/1.1");

        //when
        UserProcessor.userCreate(requestObject);
        Collection<User> result = Database.findAll();

        //Then
        assertThat(result.toString()).isEqualTo("[User [userId=1234, password=1q2w, name=bjh3311, email=abc@naver.com]]");


    }//지금은 이 코드 하나지만 나중에 여기에 다른 코드들이 추가되면 @After 이든 @Before든 추가해서 db를 비워주면서 해야한다
}